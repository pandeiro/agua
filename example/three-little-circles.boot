#!/usr/bin/env boot

(set-env!
 :dependencies '[[pandeiro/agua "0.2.3"]
                 [reagent       "0.5.0-alpha"]])

(require '[agua.core :refer [defhtml defcljs serve]])


(defhtml page
  [:head
   [:meta {:charset "utf-8"}]
   [:title "Three Little Circles"]]
  [:body
   [:div#root "%s"] ; <- pre-rendered view inserted
   [:script "%s"]]) ; <- compiled raw js inserted


(defcljs app
  (ns app.core
    (:require [reagent.core :as r]))

  (def data (r/atom [32 57 112]))

  (defn svg []
    [:svg {:width 720 :height 120}
     (for [x (range (count @data))]
       [:circle
        {:cx    (+ (* x 100) 30)
         :cy    60
         :r     (.sqrt js/Math (@data x))
         :style {:fill "steelblue"}}])])

  (if js/document
    (r/render [svg] (.getElementById js/document "root"))
    (r/render-to-string [svg])))

;; Server
;;
(defn -main [& args]
  (serve {:html page, :app app}))
