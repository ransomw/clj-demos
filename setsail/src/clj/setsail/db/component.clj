(ns setsail.db.component
  (:require
   [com.stuartsierra.component :as component]
   [jdbc.core :as jdbc]
   [hugsql.core :as hugsql]
   [hugsql.adapter.clojure-jdbc :as cj-adapter]
   ))

(defn app-init []
  (hugsql/set-adapter! (cj-adapter/hugsql-adapter-clojure-jdbc)))

(defrecord Database [db-spec]
  component/Lifecycle
  (start [component]
    (app-init)
    (let [conn (jdbc/connection (:db-spec component))
          updated-component (assoc component :connection conn)
          ]
      updated-component
      ))
  (stop [component]
    (when-let [conn (:connection component)]
      (.close conn))
    (assoc component :connection nil)))

(defn new-database [db-spec]
  (map->Database {:db-spec db-spec}))
