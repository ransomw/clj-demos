(ns feels.application
  (:gen-class)
  (:require
   [com.stuartsierra.component :as component]
   (system.components
    [endpoint :refer [new-endpoint]]
    [handler :refer [new-handler]]
    [middleware :refer [new-middleware]]
    [http-kit :refer [new-web-server]]
    )
   [feels.config :refer [config]]
   [feels.routes :refer [home-routes]]
   [feels.db.component :refer [new-database]]
   ))

(defn app-system [config]
  (component/system-map
   :db (new-database (:db-spec config))
   :routes (component/using (new-endpoint home-routes) [:db])
   :middleware (new-middleware {:middleware (:middleware config)})
   :handler (-> (new-handler)
                (component/using [:routes :middleware]))
   :http (-> (new-web-server (:http-port config))
             (component/using [:handler]))
   ))

(defn -main [& _]
  (let [config (config)]
    (-> config
        app-system
        component/start)
    (println "Started feels on" (str "http://localhost:" (:http-port config)))))
