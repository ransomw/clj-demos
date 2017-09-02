(ns styles.setsail.fonts
  (:require
   [garden.stylesheet :refer [at-font-face]]

   [setsail.styles.fonts :refer [custom-font-faces]]
  ))

(def font-path "/fonts")

(def font-faces
  (map
   (fn [font-name]
     (at-font-face
      {
       :font-family (str "'" font-name "'")
       :font-style "normal"
       :font-weight "400"
       :src (str "local('" font-name "'), "
                 "url(\"" font-path "/" font-name ".ttf\")")
       }))
   (vals custom-font-faces)
   ))
