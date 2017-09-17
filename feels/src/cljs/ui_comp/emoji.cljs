(ns feels.ui-comp.emoji
  (:require
   [rum.core :as rum]

   [feels.ui-comp.smiley :refer [emoji-face]]
   ))

(defonce mood-vecs (list :happy :sleepy :grumpy))

(defn slider-input [mood key]
  [:div {:key (name key)}
   [:span (str (name key) ":")]
   [:input {:type "range" :min 0 :max 10 :step 1
            :value (key @mood)
            :onChange (fn [ev]
                        (swap! mood assoc key
                               (-> ev .-target .-value))
                        )
            }]
   ])

(rum/defcs mood-sliders <
  (rum/local (zipmap mood-vecs (repeat (count mood-vecs) 0)) :mood)
  [state _]
  (let [mood (:mood state)
        slider-input (partial slider-input mood)]
    [:div
     (emoji-face @mood)
     [:div (map slider-input mood-vecs)]
     ]))
