(set-env!
 :source-paths #{"src"}
 :dependencies '[[org.clojure/clojure          "1.7.0-alpha5" :scope "provided"]
                 [org.clojure/clojurescript    "0.0-2760"]
                 [cljsjs/react                 "0.12.2-5"]
                 [reagent                      "0.5.0-alpha"]
                 [ring/ring-jetty-adapter      "1.3.2"]
                 [rm-hull/ring-gzip-middleware "0.1.7"]
                 [hiccup                       "1.0.5"]
                 [garden                       "1.2.5"]
                 [adzerk/bootlaces             "0.1.9" :scope "test"]])

(require '[adzerk.bootlaces :refer :all])

(def +version+ "0.1.0")

(bootlaces! +version+)

(task-options!
 pom {:project     'pandeiro/agua
      :version     +version+
      :description "Helpers for just-add-water webapps"
      :url         "https://github.com/pandeiro/agua"
      :scm         {:url "https://github.com/pandeiro/agua"}
      :license     {"Eclipse Public License" "http://www.eclipse.org/legal/epl-v10.html"}})
