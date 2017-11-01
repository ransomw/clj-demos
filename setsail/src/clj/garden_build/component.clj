(ns garden-build.component
  (:require
   [com.stuartsierra.component :as component]
   [garden-build.core :refer [start-build! stop-build!]]
  ))

(defrecord GardenBuild [build-opts garden-opts]
  component/Lifecycle
  (start [component]
    (let [watcher (start-build! build-opts garden-opts)
          updated-component (assoc component :watcher watcher)
          ]
      updated-component
      ))
  (stop [component]
    (when-let [watcher (:watcher component)]
      (stop-build! watcher))
    (assoc component :watcher nil)))

(defn new-garden-build [build-opts garden-opts]
  (map->GardenBuild {:build-opts build-opts
                     :garden-opts garden-opts
                     }))
