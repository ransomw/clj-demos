(ns feels.devcards.util
  (:require-macros
   [devcards.core :as dc :refer [dom-node]]
   )
  (:require
   [devcards.core]
   [rum.core :as rum]
   ))

(defn rum-mount [comp]
  (dom-node
   (fn [data-atom node]
      (rum/mount (comp @data-atom) node)
     )
   ))
