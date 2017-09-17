(ns feels.devcards.emoji
  (:require-macros
   [devcards.core :as dc :refer
    [defcard defcard-doc noframe-doc deftest]]
   [cljs.test :refer [is testing]]
   )
  (:require
   [devcards.core]

   [feels.ui-comp.emoji :as emoji]
   [feels.devcards.util :refer [rum-mount]]
   ))


(defcard
  "**the perfect person**"
  (rum-mount emoji/mood-sliders)
  {}
  {:inspect-data true}
  )
