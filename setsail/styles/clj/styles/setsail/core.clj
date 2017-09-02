(ns styles.setsail.core
  (:require
   [garden.core] ;; removing breaks 'at-media
   [garden.stylesheet :refer [at-media]]

   [setsail.styles.common :as c]
   [styles.setsail.fonts :refer [font-faces]]
  ))

(def header-colors
  (list
   [:h1 {:color "blue"}]
   [:h2 {:color "purple"}]
   [:h3 {:color "red"}]
   ))

(def interactive-section-style
  (list
   [:section#interactive
    {
     :background "azure"
     :padding "1.5rem"
     :border-radius "1rem"
     :margin-left "15%"
     :margin-right "15%"
     }
    ]
   (at-media {:max-width "600px"}
             [:section#interactive
              {:border-radius "0"
               :margin-left "0"
               :margin-right "0"
               }])
   ))

(def ^:garden main
  (->> (list)
       (into font-faces)
       (into header-colors)
       (into c/global-styles)
       (into interactive-section-style)
       ))
