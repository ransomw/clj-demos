(ns feels.ui-comp.emoji
  (:require
   [rum.core :as rum]

   [feels.ui-comp.smiley :refer [emoji-face]]
   ))

(defonce mood-vecs (list :happy :sleepy :grumpy))

(defn slider-input [props key]
  [:div {:key (name key)}
   [:span (str (name key) ":")]
   [:input {:type "range" :min 0 :max 10 :step 1
            :value (rum/react (rum/cursor-in props [:mood key]))
            :onChange
            (fn [ev]
              (if (not (deref (rum/cursor-in props [:loading?])))
                ((deref (rum/cursor-in props [:update-mood]))
                 key (-> ev .-target .-value int))
                ))
            }]
   ])

(rum/defc mood-sliders <
  rum/reactive
  [props]
  [:div
   (emoji-face (rum/react (rum/cursor-in props [:mood])))
   [:div (map
          (partial slider-input props)
          mood-vecs)]
   ])
