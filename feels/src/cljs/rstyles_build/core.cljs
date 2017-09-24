(ns rstyles-build.core
  (:require
   [clojure.string :as s]
   ))

(defn upcase-initials [some-str]
  (str (s/upper-case (first some-str))
       (s/join (rest some-str)))
  )

(defn dash-to-camel-case [some-str]
  (let [var-words (s/split some-str "-")
        first-word (first var-words)
        last-words (map upcase-initials (rest var-words))]
    (str first-word (s/join last-words))
    ))

(defn garden->react [elem-style]
  {:pre [(map? elem-style)]}
  (zipmap
   (map (comp keyword dash-to-camel-case name) (keys elem-style))
   (vals elem-style))
  )
