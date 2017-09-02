(ns setsail.styles.common
  (:require
   [setsail.styles.fonts :refer [font-faces]]
   ))

(def root-rel-font-sizes-style
  (list
   [:h2 {:font-size "3rem"}]
   [:button {:font-size "1.25rem"}]
   ))

(def hs-els
  (list :h1 :h2 :h3 :h4 :h5))

(def font-face-hs-style
  (map (fn [t]
         [t {:font-family (str "'" (:3Dumb font-faces) "'")}]
         )
       hs-els)
  )

(def underline-hs-style
  (map (fn [t] [t {:text-decoration "underline"}]) hs-els)
  )

(def center-hs-style
  (map (fn [t] [t {:text-align "center"}]) hs-els)
  )

(def default-list-style
  (list
   [:ul {:list-style "none"
         :padding "0"
         }]
   ))

(def button-style
  (list
   [:button
    {:border-radius "1em"
     :font-family (str "'" (:Aaargh font-faces) "'")
     :font-weight 900
     :font-size "1.5rem"
     :margin-left ".5em"
     :margin-right ".5em"
     :border "solid .05em"
     :color "black"
     :border-color "black"
     :background "cyan"}]
   [:button:hover
    {:cursor "pointer"
     :background "blue"
     :color "coral"
     :border-color "coral"
     }]
   ))

(def section-style
  (list
   [:section
    {:display "flex"
     :flex-direction "column"
     :justify-content "center"
     :align-items "center"
     }]
   ))

(def footer-style
  (list
   [:footer
    {:text-align "right"
     :font-style "italic"
     :background "moccasin"
     :padding-top ".5em"
     :padding-bottom "1.5em"
     :padding-left "1em"
     :padding-right "1em"
     :margin-top "1.5rem"
     :margin-bottom "0"
     }]
   ))

(def video-style
  (list
   [:div.video-container
    {:display "flex"
     :justify-content "center"
     :position "relative"
     }
    [:.play
     {:color "cyan"
      }]
    [:.play:hover
     {:cursor "pointer"
      :color "blue"
      }]
    ]
   [:div.video-overlay
    {:position "absolute"
     :top "0" :left "0" :bottom "0" :right "0"
     :background "seagreen"
     :opacity "0.6"
     :display "flex"
     :justify-content "center"
     :align-items "center"
     }]
   [:video
    {:max-width "100%"
     :height "auto"
     }]
   ))

(def global-styles
  (->> (list)
       (into root-rel-font-sizes-style)
       (into underline-hs-style)
       (into font-face-hs-style)
       (into center-hs-style)
       (into section-style)
       (into default-list-style)
       (into button-style)
       (into video-style)
       (into footer-style)
       ))
