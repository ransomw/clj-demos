(ns user
  (:require
   [setsail.server]
   [ring.middleware.reload :refer [wrap-reload]]
   [figwheel-sidecar.repl-api :as figwheel]
   [figwheel-sidecar.config :as figwheel-config]
   [clojure.java.io :as io]

   [garden-build.core :refer [start-build! stop-build!]
    :rename {start-build! start-css-build!
             stop-build! stop-css-build!}]
   ))

;; Let Clojure warn you when it needs to reflect on types, or when it does math
;; on unboxed numbers. In both cases you should add type annotations to prevent
;; degraded performance.
(set! *warn-on-reflection* true)
(set! *unchecked-math* :warn-on-boxed)
(def http-handler
  (wrap-reload #'setsail.server/http-handler))

(defn run []
  (start-css-build!
   {:source-paths ["styles/clj/" "styles/cljc"]
    :style-ns 'styles.setsail.core
    }
   {
    :output-to "resources/public/css/compiled/garden-main.css"
    :pretty-print? true
    })
  (figwheel/start-figwheel!
   (merge
    (get-in (figwheel-config/fetch-config) [:data :figwheel-options])
    {:builds (figwheel-config/get-project-builds)
     :ring-handler 'user/http-handler
     :builds-to-start ["app"]}
    ))
   )

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
  (stop-css-build!)
  (figwheel/stop-figwheel!))

;; this could be placed in ns declaration?
(defn init-require []
  (require 'setsail.routes.helpers :reload)
  (require 'setsail.routes.core :reload)
  (require '(setsail.db [core :as db]) :reload)
  )

(init-require)

(defn reload-require []
  (require 'user :reload)
  (require 'setsail.routes.helpers :reload)
  (require 'setsail.routes.core :reload)
  (require 'setsail.db.core :reload)
  (require 'garden-build.core :reload)
  )

(def rreq reload-require)
(def rdb db/reset-db!)
(def brep browser-repl)

(defn enter-ns [namespace]
  (require namespace :reload-all)
  (in-ns namespace))
