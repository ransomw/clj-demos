(ns feels.devcards.smiley
  (:require-macros
   [devcards.core :as dc :refer
    [defcard defcard-doc noframe-doc deftest]]
   [cljs.test :refer [is testing]]
   )
  (:require
   [devcards.core]

   [feels.ui-comp.smiley :as smile]
   [feels.devcards.util :refer [rum-mount]]
   ))

(defcard
  "**the perfect person**"
  (rum-mount smile/emoji-face)
  {:happy 10 :sleepy 0 :grumpy 0}
  {:inspect-data true}
  )

(defcard
  "**another dwarf (sleepy)**"
  (rum-mount smile/emoji-face)
  {:happy 0 :sleepy 10 :grumpy 0}
  {:inspect-data true}
  )

(defcard
  "**another dwarf (grumpy)**"
  (rum-mount smile/emoji-face)
  {:happy 0 :sleepy 0 :grumpy 10}
  {:inspect-data true}
  )

(defcard
  "**mild-mannered Clark Kent**"
  (rum-mount smile/emoji-face)
  {:happy 5 :sleepy 5 :grumpy 5}
  {:inspect-data true}
  )

(defcard
  "**flatline**"
  (rum-mount smile/emoji-face)
  {:happy 0 :sleepy 0 :grumpy 0}
  {:inspect-data true}
  )

(defcard
  "**ALL TEH THINGS**"
  (rum-mount smile/emoji-face)
  {:happy 10 :sleepy 10 :grumpy 10}
  {:inspect-data true}
  )
