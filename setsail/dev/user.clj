(ns user
  (:require
   [clojure.repl :refer [doc]]
   [clojure.tools.namespace.repl
    :refer [set-refresh-dirs refresh refresh-all]]
   [com.stuartsierra.component :as component]
   [reloaded.repl :refer [system init]]
   [figwheel-sidecar.repl-api :as figwheel]
   [figwheel-sidecar.config :as fw-config]
   [figwheel-sidecar.system :as fw-sys]

   [garden-build.component :refer [new-garden-build]]
   [setsail.application]
   [setsail.config :refer [config]]
   [setsail.db.core :refer [reset-db!]]
   ))

(set! *warn-on-reflection* true)
(set! *unchecked-math* :warn-on-boxed)

(defn dev-system []
  (assoc (setsail.application/app-system (config))
    :figwheel-system (fw-sys/figwheel-system (fw-config/fetch-config))
    :css-watcher (fw-sys/css-watcher
                  {:watch-paths ["resources/public/css"]})
    :garden-build
    (new-garden-build
     {:source-paths ["styles/clj/" "styles/cljc"]
      :style-ns 'styles.setsail.core
      }
     {
      :output-to "resources/public/css/compiled/garden-main.css"
      :pretty-print? true
      })
    ))

(defn devcards-system []
  (assoc
   (setsail.application/app-system (config))
   :figwheel-system
   (fw-sys/figwheel-system
    (-> (fw-config/fetch-config)
        (update-in
         [:data :figwheel-options]
         #(merge % {:http-server-root "devcards"}))
        (update-in
         [:data]
         #(merge % {:build-ids ["devcards"]}))
        )
    )))

(set-refresh-dirs "src" "dev" "test")

(defn connect-and-reset-db! []
  (let [db (-> (config)
               setsail.application/app-system
               :db component/start)]
    (reset-db! db)
    (component/stop db)
    ))

(defn cljs-repl []
  (fw-sys/cljs-repl (:figwheel-system system)))

(def stop reloaded.repl/stop)
(defn go []
  (reloaded.repl/set-init! #(dev-system))
  (reloaded.repl/go))
(defn go-devcards []
  (reloaded.repl/set-init! #(devcards-system))
  (reloaded.repl/go))

(defn cljs-clean []
  (figwheel/start-figwheel!)
  (figwheel/clean-builds :app :devcards)
  (figwheel/stop-figwheel!)
  )

(def rdb connect-and-reset-db!)
