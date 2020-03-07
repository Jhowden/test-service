(ns views.content
    (:require [hiccup.element :as he]))

(defn not-found-text []
  [:div
    [:h1 {:class "info-warning"} "Page Not Found"]
    [:p "There's no requested page. "
      (he/link-to {:class "btn btn-primary"} "/" "Take me to Home")]])