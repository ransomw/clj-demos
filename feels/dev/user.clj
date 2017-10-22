(ns user
  (:require
   [clojure.repl :refer [doc]]
   [com.stuartsierra.component :as component]
   [figwheel-sidecar.config :as fw-config]
   [figwheel-sidecar.system :as fw-sys]
   [clojure.tools.namespace.repl
    :refer [set-refresh-dirs refresh refresh-all]]
   [reloaded.repl :refer [system init]]
   [ring.middleware.reload :refer [wrap-reload]]
   [figwheel-sidecar.repl-api :as figwheel]
   [clojure.test :refer [run-tests test-vars]]

   [garden-build.component :refer [new-garden-build]]
   [feels.application]
   [feels.config :refer [config]]
   [feels.routes-test]
   [feels.db-test]
   [feels.e2e-test]
   ))

(defn dev-system []
  (assoc (feels.application/app-system (config))
    :figwheel-system (fw-sys/figwheel-system (fw-config/fetch-config))
    :css-watcher (fw-sys/css-watcher {:watch-paths ["resources/public/css"]})
    :garden-build
    (new-garden-build
     {:source-paths ["styles/clj/" "styles/cljc"]
      :style-ns 'styles.feels.core
      }
     {
      :output-to "resources/public/css/compiled/garden-main.css"
      :pretty-print? true
      })
    ))

(defn devcards-system []
  (assoc
   (feels.application/app-system (config))
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

(defn cljs-repl []
  (fw-sys/cljs-repl (:figwheel-system system)))

;; Set up aliases so they don't accidentally
;; get scrubbed from the namespace declaration
(def start reloaded.repl/start)
(def stop reloaded.repl/stop)
(defn go []
  (reloaded.repl/set-init! #(dev-system))
  (reloaded.repl/go))
(defn go-devcards []
  (reloaded.repl/set-init! #(devcards-system))
  (reloaded.repl/go))
(def reset reloaded.repl/reset)
(def reset-all reloaded.repl/reset-all)

(defn browser-repl []
  (println "(browser-repl) is deprecated, use (cljs-repl)")
  (cljs-repl))

(defn run-all-tests [& opts]
  (do
    (stop)
    ;; refresh-all in case of sql changes
    (refresh-all)
    (map run-tests
         [
          'feels.e2e-test
          'feels.db-test
          'feels.routes-test
          ])
    ))

(defn cljs-clean []
  (figwheel/start-figwheel!)
  (figwheel/clean-builds)
  (figwheel/stop-figwheel!)
  )
