(ns feels.ui-comp.smiley
  (:require
   [rum.core :as rum]
   ;; js lib
   [emoji]
   ))

(rum/defcs emoji-face [_ props]
  [:div {:style (clj->js {:dislay "flex" :flexDirection "column"})}
   [:canvas {:width 300 :height 300
             :style (clj->js {:border "solid 1px black"})
             :ref (fn [el]
                    (if el
                      (emoji/draw
                       (clj->js
                        (select-keys props [:happy :sleepy :grumpy]))
                       el)))
             }
    ]
   ]
  )
