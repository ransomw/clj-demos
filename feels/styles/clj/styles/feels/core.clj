(ns styles.feels.core
  (:require
   [feels.styles.common :refer [global-styles]]
  ))

(def header-colors
  (list
   [:h1 {:color "blue"}]
   [:h2 {:color "purple"}]
   [:h3 {:color "red"}]
   ))

(def ^:garden main
  (->> (list)
       (into header-colors)
       (into global-styles)
       ))
