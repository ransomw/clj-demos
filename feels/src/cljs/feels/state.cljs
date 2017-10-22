(ns feels.state
  (:require
   [citrus.core :as citrus]
   [rum.core :refer [cursor-in]]
   [cljs-time.core :as time]

   [feels.routes :as routes]
   [feels.controllers.user-feels :as user-feels]
   [feels.controllers.user-today-feels :as user-today-feels]
   [feels.comm.core :refer [comm-effects]]
   ))

(defn update-user-today-feels-effect
  [r _ feels]
  (citrus/dispatch! r :user-feels :update-today
                    {:date (time/today)
                     :feels feels
                     }))

(defonce app-state
  (atom {}))

(defonce reconciler
  (citrus/reconciler
   {:state
    app-state
    :controllers
    {:router routes/control
     :user-feels user-feels/control
     :user-today-feels user-today-feels/control
     }
    :effect-handlers
    {:comm comm-effects
     :update-user-today-feels update-user-today-feels-effect
     }
    }))
