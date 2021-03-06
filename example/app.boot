#!/usr/bin/env boot

(set-env!
 :dependencies '[[pandeiro/agua "0.2.3"]
                 [reagent       "0.5.0-alpha"]])

(require
 '[agua.core    :refer [defhtml defcss defcljs serve]]
 '[garden.color :refer [rgb]]
 '[garden.units :refer [px]])


(defhtml page
  [:head
   [:meta {:charset "utf-8"}]
   [:title "My App"]
   [:style "%s"]]            ; <- styles inserted
  [:body
   [:div#root "%s"]          ; <- pre-rendered view inserted
   [:footer "copyright you"]
   [:script "%s"]])          ; <- compiled raw js inserted


(defcss styles
  [:*
   {:box-sizing    :border-box}]
  [:body
   {:padding       (px 36)
    :background    :black
    :color         :lightgray
    :text-align    :center}]
  [:h1
   {:color         :white}]
  [:input
   {:font-size     (px 24)
    :background    (rgb 40 40 40)
    :border        "solid 1px white"
    :font-style    :italic
    :padding       (px 12)
    :color         (rgb 150 150 150)
    :border-radius (px 8)}]
  [:footer
   {:position      :fixed
    :bottom        0
    :left          0
    :padding       (px 24)
    :width         "100%"
    :font-family   :monospace}])


(defcljs app
  (ns app.core
    (:require [reagent.core :as r]))

  (def state
    (r/atom
     {:person "person"}))

  (defn- person-val [e]
    (or (not-empty (.-value (.-target e))) "person"))

  (defn main [state]
    [:div
     [:h1
      (str "Hello, " (:person @state))]
     (when js/document
       [:input
        {:placeholder "Name, please"
         :on-change #(swap! state assoc :person (person-val %))}])])

  (defn not-found []
    [:div
     [:h1
      (str "404: '" js/location.pathname "' wasn't found.")]])

  (defn router []
    (condp re-find js/location.pathname
      #"^/$" [main state]
      [not-found]))

  (if js/document
    (r/render [router] (.getElementById js/document "root"))
    (r/render-to-string [router])))

;; Server
;;
(defn -main [& args]
  (serve {:html page, :css styles, :app app}))
