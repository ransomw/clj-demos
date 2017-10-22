(ns feels.components.ui
  (:require
   [com.stuartsierra.component :as component]
   [citrus.core :as citrus]

   [feels.core :refer [render]]
   [feels.routes :refer [start-router!]]
   [feels.state :refer [reconciler]]
   ))

(defrecord UIComponent []
  component/Lifecycle
  (start [component]
    (citrus/broadcast-sync! reconciler :init)
    (start-router! #(citrus/dispatch! reconciler :router :push %))
    (doall (map
            #(citrus/dispatch-sync! reconciler % :load)
            [:user-feels :user-today-feels]))
    (render)
    component)
  (stop [component]
    component))

(defn new-ui-component []
  (map->UIComponent {}))
