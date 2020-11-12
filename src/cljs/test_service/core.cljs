
(ns test-service.core
  (:require
   [secretary.core :as secretary :refer-macros [defroute]]
   [reagent.core :as r :refer [atom]]
   [reagent.dom :as rdom]))

(.log js/console "Hey Seymore! wts goin' on?")

(defroute "/" []
  (.log js/console "Made it to the frontend route"))

(defonce app-state (atom {:text "Hello world!"}))

(defn get-app-element []
  (.getElementById js/document "app"))

(defn hello-world []
  [:div
   [:h1 (:text @app-state)]
   [:p "This will show up 4"]])

(defn mount [el]
  (rdom/render [hello-world] el))

(defn mount-app-element []
  (when-let [el (get-app-element)]
    (mount el)))

(mount-app-element)
