(ns feels.controllers.user-today-feels)

(def initial-state
  {:feels nil
   :err nil
   :loading? false})

(defmulti control (fn [event] event))

(defmethod control :init []
  {:state initial-state})

(defmethod control :load [_ _ state]
  {:state (assoc state :loading? true)
   :comm  {:action :get-user-today-feels
           :on-load :load-ready
           :on-err :load-err
           }})

(defmethod control :set [_ [feels] state]
  {:state (assoc state :loading? true)
   :comm  {:action :set-user-today-feels
           :data feels
           :on-load :load
           :on-err :load-err
           }})

(defmethod control :load-ready [_ [{:keys [feels]}] state]
  {:state
   (-> state
       (assoc :feels feels)
       (assoc :loading? false))
   :update-user-today-feels feels
   })

(defmethod control :load-err [_ [{:keys [err]}] state]
  {:state
   (-> state
       (assoc :err err)
       (assoc :loading? false))})
