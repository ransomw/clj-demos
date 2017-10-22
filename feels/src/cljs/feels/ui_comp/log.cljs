(ns feels.ui-comp.log
  (:require
   [rum.core :as rum]
   [citrus.core :as citrus]
   [cljs-time.core :as time]

   [feels.ui-comp.calendar :as cal]
   ))

(defn make-cal-page-atom [months-back-atom r]
  (rum/derived-atom
   [months-back-atom
    (citrus/subscription
     r [:user-feels :feels])]
   ::cal-props
   (fn [months-back feels-coll]
     {:month (time/minus
              (time/first-day-of-the-month (time/today))
              (time/months months-back))
      :feels-coll feels-coll
      })
   ))

(rum/defcs log <
  rum/reactive
  (rum/local 0 ::months-back)
  [state r]
  (let [{months-back-atom ::months-back
         } state
        cal-page-atom (make-cal-page-atom
                       months-back-atom r)
        ]
    [:div
     [:div
      [:button {:onClick #(swap! months-back-atom inc)
                } "<"]
      (if (not (= 0 (rum/react months-back-atom)))
        [:button {:onClick #(swap! months-back-atom
                                   (comp (partial max 0) dec))
                  } ">"])
      ]
     [:div
      (cal/page (rum/react cal-page-atom))
      ]
     ]))
