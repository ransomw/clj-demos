(ns feels.components.ui
  (:require
   [com.stuartsierra.component :as component]
   [feels.core :refer [render]]
   [feels.routes :refer [init-routes]]
   ))

(defrecord UIComponent []
  component/Lifecycle
  (start [component]
    (init-routes)
    (render)
    component)
  (stop [component]
    component))

(defn new-ui-component []
  (map->UIComponent {}))
