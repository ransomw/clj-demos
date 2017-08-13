(ns user
  (:require
   [emcg.server]
   [ring.middleware.reload :refer [wrap-reload]]
   [figwheel-sidecar.repl-api :as figwheel]
   [figwheel-sidecar.config :as figwheel-config]
   [clojure.java.io :as io]
   ))

;; Let Clojure warn you when it needs to reflect on types, or when it does math
;; on unboxed numbers. In both cases you should add type annotations to prevent
;; degraded performance.
(set! *warn-on-reflection* true)
(set! *unchecked-math* :warn-on-boxed)
(def http-handler
  (wrap-reload #'emcg.server/http-handler))

(defn run []
  (figwheel/start-figwheel!
   {:builds (figwheel-config/get-project-builds)
    :ring-handler 'user/http-handler
    :builds-to-start ["app"]}
   ))

(def browser-repl figwheel/cljs-repl)

(defn run-devcards []
  (figwheel/start-figwheel!
   {:builds (figwheel-config/get-project-builds)
    :http-server-root "devcards"
    :css-dirs ["resources/public/css"]
    :builds-to-start ["devcards"]}
   ))

(defn build-cljs []
  (figwheel/clean-builds :app :devcards))

(defn stop []
  (figwheel/stop-figwheel!))

(defn init-require []
  (require 'emcg.hroutes :reload)
  (require 'emcg.routes.core :reload)
  (require '(emcg.demo [db :as db]) :reload)
  )

(init-require)

(defn reload-require []
  (require 'user :reload)
  (require 'emcg.hroutes :reload)
  (require 'emcg.routes.core :reload)
  (require 'emcg.demo.db :reload)
  )

(def rreq reload-require)
(def rdb db/reset-db!)
(def brep browser-repl)

(defn enter-ns [namespace]
  (require namespace :reload)
  (in-ns namespace))
