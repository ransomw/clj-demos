(defproject setsail "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies
  [
   ;;;;;;;; from chestnut
   [org.clojure/clojure "1.8.0"]
   [org.clojure/clojurescript "1.9.908" :scope "provided"]
   [com.cognitect/transit-clj "0.8.285"]
   [ring "1.4.0"]
   [ring/ring-defaults "0.2.0"]
   [ring/ring-mock "0.3.1"]
   [bk/ring-gzip "0.1.1"]
   [ring.middleware.logger "0.5.0"]
   [compojure "1.5.0"]
   [environ "1.0.3"]
   [org.omcljs/om "1.0.0-alpha36"]
   ;;;;;;;; added to chestnut
   [com.stuartsierra/component "0.3.2"]
   [org.danielsz/system "0.4.0"]
   [org.clojure/tools.namespace "0.2.11"]
   [http-kit "2.2.0"]
   ;;;; client
   [cljs-http "0.1.42"]
   ;;;; server
   [postgresql "9.3-1102.jdbc41"]
   [com.layerware/hugsql "0.4.7"]
   [com.layerware/hugsql-adapter-clojure-jdbc "0.4.7"]
   ;; explicitly specify to avoid
   ;;  namespace 'cheshire.factory' not found
   ;; error with ring-middleware-format add
   [cheshire "5.7.0"]
   [ring-middleware-format "0.7.2"]
   ;; todo: dedupe plugin?
   ;; duplicates plugin version
   [lein-doo "0.1.6"]
   [devcards "0.2.3"]
   [reagent "0.7.0"]
   [garden "1.3.2"]
   [hawk "0.2.11"]
   [sablono "0.8.0"]
   ]

  :plugins
  [[lein-cljsbuild "1.1.3"]
   [lein-environ "1.0.3"]
   ]

  :min-lein-version "2.6.1"

  :source-paths ["src/clj" "src/cljs" "src/cljc"
                 "styles/clj" "styles/cljs" "styles/cljc"
                 ]

  :test-paths ["test/clj" "test/cljc"]

  :clean-targets ^{:protect false}
  [:target-path :compile-path
   "resources/public/js"
   "resources/devcards/js"]

  :uberjar-name "setsail.jar"

  ;; Use `lein run` if you just want to start a HTTP server, without figwheel
  :main "setsail.server"

  ;; nREPL by default starts in the :main namespace, we want to start in `user`
  ;; because that's where our development helper functions like (run) and
  ;; (browser-repl) live.
  :repl-options
  {
   :init-ns user
   :timeout 120000 ;; ms 30000 default
   }

  :cljsbuild
  {:builds
   [{:id "app"
     ;; clj test env keeps local db
     ;; cljs production env connects to full backend
     :source-paths ["src/cljs" "src/cljc"
                    "env/prod/cljs" "env/test/clj"
                    "styles/cljs" "styles/cljc"
                    ]
     :figwheel true
     ;; Alternatively,
     ;; :figwheel {:on-jsload "setsail.core/on-figwheel-reload"}
     :compiler {:main setsail.core
                :asset-path
                "js/compiled/out"
                :output-to
                "resources/public/js/compiled/setsail.js"
                :output-dir
                "resources/public/js/compiled/out"
                :source-map-timestamp true}}

    {:id "devcards"
     :source-paths ["src/cljs" "src/cljc"
                    "env/test/cljs" "env/test/clj"
                    "styles/cljs" "styles/cljc"
                    "test/cljs" "test/cljc"
                    ]
     :figwheel {:devcards true}
     :compiler
     {
      :main setsail.devcards
      :asset-path
      "js/compiled/devcards_out"
      :output-to
      ;; matches http-server-root for figwheel
      "resources/devcards/js/compiled/setsail_devcards.js"
      :output-dir
      "resources/devcards/js/compiled/devcards_out"
      :source-map-timestamp true}}

    {:id "test"
     :source-paths ["src/cljs" "test/cljs"
                    "styles/cljs" "styles/cljc"
                    "src/cljc" "test/cljc"]
     :compiler {:output-to "resources/public/js/compiled/testable.js"
                :main setsail.test-runner
                :optimizations :none}}

    {:id "min"
     :source-paths ["src/cljs" "src/cljc"
                    "styles/cljs" "styles/cljc"
                    "env/prod/cljs"]
     :jar true
     :compiler {:main setsail.core
                :output-to "resources/public/js/compiled/setsail.js"
                :output-dir "target"
                :source-map-timestamp true
                :optimizations :advanced
                :pretty-print false}}]}

  :figwheel {:css-dirs ["resources/public/css"
                        "resources/public/css/compiled"]
             :server-logfile "log/figwheel.log"}

  :doo {:build "test"}

  :profiles
  {:dev
   {:dependencies [[figwheel "0.5.13"]
                   [figwheel-sidecar "0.5.13"]
                   [com.cemerick/piggieback "0.2.2"]
                   [org.clojure/tools.nrepl "0.2.13"]
                   [reloaded.repl "0.2.3"]
                   ]

    :plugins [[lein-figwheel "0.5.13"]
              [lein-doo "0.1.6"]]

    :source-paths ["dev" "env/test/clj"]
    :repl-options {:nrepl-middleware
                   [cemerick.piggieback/wrap-cljs-repl]}}

   :uberjar
   {:source-paths ^:replace ["src/clj" "src/cljc"]
    :prep-tasks ["compile" ["cljsbuild" "once" "min"]]
    :hooks []
    :omit-source true
    :aot :all}})

