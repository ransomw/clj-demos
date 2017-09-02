(ns setsail.styles.core
  (:require
   [clojure.string :as s]
   [setsail.styles.fonts :refer [font-faces]]
  ))

(def db-records-test-style
  {:background "lightgoldenrodyellow"
   })

(def buttons-div-style
  {:margin-bottom "1em"
   :display "flex"
   :justify-content "space-between"
   })

(def msg-list-style
  {
   :display "flex"
   :flex-direction "column"
   :font-family (str "'" (:journal font-faces) "'")
   :font-size "2rem"
   :background "lightgoldenrodyellow"
   :padding "1.5em"
   :border-radius ".25em"
   })

(def play-style
  {:font-family (str "'" (:3Dumb font-faces) "'")
   :font-size "1.75em"
   })

(defn img-test-el-style [anim-per]
  ;; expanding /ann/uli /anim/ation
  (let [anim-per-float (/ anim-per 100.)
        ann-width 12
        ann-offset (* ann-width anim-per-float 10)
        inner-rim ann-offset
        middle-rim (+ ann-offset ann-width)
        outer-rim (+ ann-offset (* 2 ann-width))]
    {:padding "3em"
     :display "flex"
     :background
     (str "repeating-radial-gradient("
          (s/join ","
                  (list "circle"
                        (str "purple " (str inner-rim) "px")
                        (str "purple " (str middle-rim) "px")
                        (str "#4b026f " (str middle-rim) "px")
                        (str "#4b026f " (str outer-rim) "px")
                        ))
          ")")
     :justify-content "center"
     }))

(def img-el-style
  {:border ".25em solid"
   :border-color "black"
   })
