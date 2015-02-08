(.put (System/getProperties) "org.eclipse.jetty.LEVEL" "WARN") ; silence jetty

(ns agua.core
  (:import  [javax.script         ScriptEngineManager]
            [java.io              File])
  (:require [clojure.java.io      :as io]
            [clojure.string       :as s]
            [cljs.closure         :as cljsc]
            [hiccup.page          :refer [html5]]
            [garden.core          :refer [css]]
            [ring.adapter.jetty   :refer [run-jetty]]
            [ring.middleware.gzip :refer [wrap-gzip]]))

(def ^:private reactjs
  (slurp (io/resource "cljsjs/production/react.min.inc.js")))

(def ^:private nashorn
  (.getEngineByName (ScriptEngineManager.) "nashorn"))

(defn- cljs->js [str]
  (print "Compiling ClojureScript... ")
  (let [in (File/createTempFile "cljs-in" "cljs")]
    (spit in str)
    (let [result (with-out-str
                   (cljsc/build in {:output-to :print, :optimizations :simple}))]
      (println "done.")
      result)))

(def prerender
  (memoize
   (fn [html react-app css uri]
     (when (not= uri "/favicon.ico")
       (print (format "Prerendering %s... " uri))
       (flush))
     (let [preamble  "var global = this, document = null;" ; missing in Nashorn env
           route     (str "var location = {pathname: '" uri "'};")
           server-js (str preamble route reactjs react-app)
           view      (.eval nashorn server-js)
           client-js (str reactjs react-app)]
       (when (not= uri "/favicon.ico")
         (println "done."))
       (format html css view client-js)))))

;;
;; API
;;

(defmacro defhtml [sym & body]
  `(def ~sym ~(html5 body)))

(defmacro defcljs [sym & body]
  `(def ~sym
     ~(->> body
        (map str)
        (apply str)
        cljs->js)))

(defmacro defcss [sym & body]
  `(def ~sym
     ~(->> body
        (map css)
        (s/join #" "))))

(defn serve
  "The serve function takes a map with :html, :app, :css
  and, optionally, :port keys, and launches a server that
  will respond with a server-rendered version of the page,
  including its script component for dynamic behavior
  on the client side.

  :html - a string representing the page HTML. Will be passed
  to a format function that expects three '%s' to substitute,
  in the following order: CSS, pre-rendered view HTML, and
  compiled javascript.

  :app - the compiled javascript as a string. This will be included
  in the page both as a pre-rendered view and as raw script.

  :css - the page's styles as a string.

  :port - (optional) defaults to 6060."
  [{:keys [html app css port]}]
  (let [handler (fn [req]
                  {:status  200
                   :headers {"Content-Type" "text/html"}
                   :body    (prerender html app css (:uri req))})
        port    (or port 6060)]
    (println (format "Server started at http://localhost:%d." port))
    (run-jetty (wrap-gzip handler) {:port port})))
