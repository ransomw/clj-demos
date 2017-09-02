(ns rstyles-build.core
  (:require
   [clojure.string :as s]
   [clojure.walk :as w]
   [garden.core :as g]
   ))

;; much lifted from https://github.com/bensu/hickory-stack

(defn string->tokens
  "Takes a string with syles and parses it into properties and value tokens"
  [style]
  {:pre [(string? style)]
   :post [(even? (count %))]}
  (->> (s/split style #";")
       (mapcat #(s/split % #":"))
       (map s/trim)))


(defn tokens->map
  [tokens]
  {:pre [(even? (count tokens))]
   :post [(map? %)]}
  (zipmap (keep-indexed #(if (even? %1) %2) tokens)
          (keep-indexed #(if (odd? %1) %2) tokens)))

(defn str->react
  [style]
  (tokens->map (string->tokens style)))

(defn css [data]
  (str->react (g/css data)))

;;;;;; much sand

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
