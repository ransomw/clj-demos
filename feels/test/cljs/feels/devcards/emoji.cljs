(ns feels.devcards.emoji
  (:require-macros
   [devcards.core :as dc :refer
    [defcard defcard-doc noframe-doc deftest]]
   [cljs.test :refer [is testing]]
   )
  (:require
   [devcards.core]
   [rum.core :as rum]

   [feels.ui-comp.emoji :as emoji]
   [feels.devcards.util :refer [rum-mount]]
   [feels.dat-checks :refer [example-feels]]
   ))

(defonce mood-atom (atom example-feels))

(defcard
  "**the perfect person**"
  (rum-mount emoji/mood-sliders :as-atom true)
  (rum/derived-atom
   [mood-atom]
   ::emoji-creator-props
   (fn [mood]
     {:mood mood :loading? false
      :update-mood (fn [key val] (swap! mood-atom assoc key val))
      }))
  {:inspect-data true}
  )
