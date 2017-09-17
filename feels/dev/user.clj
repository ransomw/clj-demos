(ns user
  (:require
   [com.stuartsierra.component :as component]
   [figwheel-sidecar.config :as fw-config]
   [figwheel-sidecar.system :as fw-sys]
   [clojure.tools.namespace.repl :refer [set-refresh-dirs]]
   [reloaded.repl :refer [system init]]
   [ring.middleware.reload :refer [wrap-reload]]
   [figwheel-sidecar.repl-api :as figwheel]
   [garden-watcher.core :refer [new-garden-watcher]]
   [clojure.test :refer [run-tests test-vars]]

   [feels.application]
   [feels.config :refer [config]]
   [feels.routes-test]
   ))

(defn dev-system []
  (assoc (feels.application/app-system (config))
    :figwheel-system (fw-sys/figwheel-system (fw-config/fetch-config))
    :css-watcher (fw-sys/css-watcher {:watch-paths ["resources/public/css"]})
    :garden-watcher (new-garden-watcher ['feels.styles])))

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
  (map (fn [test-ns]
         (if (contains? (set opts) :reload)
           (do (println "loading" test-ns)
               (require test-ns :reload-all)))
           (run-tests test-ns))
       ['feels.routes-test])
  )
