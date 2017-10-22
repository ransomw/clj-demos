(ns feels.controllers.user-feels
  (:require
   [cljs-time.core :refer [equal?] :rename {equal? time-equal?}]
   ))

(def initial-state
  {:feels (list)
   :err nil
   :loading? false
   })

(defmulti control (fn [event] event))

(defmethod control :init []
  {:state initial-state})

(defmethod control :load [_ _ state]
  {:state (assoc state :loading? true)
   :comm  {:action :get-user-feels
           :on-load :load-ready
           :on-err :load-err
           }})

(defmethod control :load-ready [_ [user-feels] state]
  {:state
   (-> state
       (assoc :feels user-feels)
       (assoc :loading? false))})

(defmethod control :load-err [_ [{:keys [err]}] state]
  {:state
   (-> state
       (assoc :err err)
       (assoc :loading? false))})

;; _supposing_ the server-side api had to be this ill-concieved :-|
(defmethod control :update-today [_ [{:keys [date]
                                      :as feels-in-coll}] state]
  (let [is-date? (partial time-equal? date)
        not-today-feels (filter (comp not is-date? :date)
                                (:feels state))
        updated-feels (conj not-today-feels feels-in-coll)]
    {:state
     (-> state
         (assoc :feels updated-feels)
         )
     }))
