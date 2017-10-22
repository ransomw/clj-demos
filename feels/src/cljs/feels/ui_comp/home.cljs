(ns feels.ui-comp.home
  (:require
   [rum.core :as rum]
   [citrus.core :as citrus]

   [feels.ui-comp.emoji :as emoji]
   ))

(rum/defcs home <
  {:will-mount
   (fn [{[r] :rum/args :as state}]
     (let [feels-atom (citrus/subscription
                       r [:user-today-feels :feels])
           mood-atom (atom
                      (or @feels-atom
                          (zipmap emoji/mood-vecs
                                  (repeat (count emoji/mood-vecs) 0))
                          ))]
       (add-watch
        feels-atom ::set-today-feels-watch
        (fn [_ _ feels-old feels-new]
          (if (and (not feels-old) feels-new)
            (reset! mood-atom feels-new))))
       (-> state
           (assoc ::mood mood-atom)
           (assoc ::feels feels-atom)
           )
       ))}
  rum/reactive
  [state r]
  (let [loading?-atom (citrus/subscription
                       r [:user-today-feels :loading?])
        {mood-atom ::mood
         feels-atom ::feels
         } state]
    [:div
     (emoji/mood-sliders
      (rum/derived-atom
       [mood-atom loading?-atom]
       ::emoji-creator-props
       (fn [mood loading?]
         {:mood mood :loading? loading?
          :update-mood
          (partial swap! mood-atom assoc)
          })
       ))
     (if (not (rum/react loading?-atom))
       [:button {:onClick #(citrus/dispatch!
                            r :user-today-feels :set
                            @mood-atom)
                 } (if (rum/react feels-atom)
                     "update feels"
                     "set feels")])
     ]))
