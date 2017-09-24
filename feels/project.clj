(defproject feels "0.0.0-SNAPSHOT"
  :description ""
  :dependencies
  [
   ;; from Chestnut
   [org.clojure/clojure "1.8.0"]
   [org.clojure/clojurescript "1.9.908" :scope "provided"]
   [com.cognitect/transit-clj "0.8.300"]
   [ring "1.6.2"]
   [ring/ring-defaults "0.3.1"]
   [bk/ring-gzip "0.2.1"]
   [radicalzephyr/ring.middleware.logger "0.6.0"]
   [compojure "1.6.0"]
   [environ "1.1.0"]
   [com.stuartsierra/component "0.3.2"]
   [org.danielsz/system "0.4.0"]
   [org.clojure/tools.namespace "0.2.11"]
   [http-kit "2.2.0"]
   [rum "0.10.8"]
   ;; added
   [devcards "0.2.3"]
   [ring/ring-mock "0.3.1"]
   [hickory "0.7.1"]
   [postgresql "9.3-1102.jdbc41"]
   ;; [funcool/clojure.jdbc "0.9.0"]
   [com.layerware/hugsql "0.4.7"]
   [com.layerware/hugsql-adapter-clojure-jdbc "0.4.7"]
   [venantius/accountant "0.2.0"]
   [bidi "2.1.2"]
   [hawk "0.2.11"]
   [garden "1.3.2"]
   [com.andrewmcveigh/cljs-time "0.5.0"]
   [clj-time "0.14.0"]
   ]

  :plugins
  [
   [lein-cljsbuild "1.1.6"]
   [lein-environ "1.1.0"]
   ]

  :min-lein-version "2.6.1"

  :source-paths ["src/clj" "src/cljs" "src/cljc"
                 "styles/clj" "styles/cljs" "styles/cljc"]

  :test-paths ["test/clj" "test/cljc"]

  :clean-targets ^{:protect false}
  [:target-path :compile-path
   "resources/public/js"
   "resources/devcards/js"
   ]

  :uberjar-name "feels.jar"

  ;; `lein run` to start a HTTP server without figwheel
  :main feels.application

  :repl-options
  {
   :init-ns user ;; default: main
   :timeout 120000 ;; ms ;; default: 30000
   }

  :cljsbuild
  {:builds
   [
    {:id "app"
     :source-paths ["src/cljs" "src/cljc" "dev"
                    "styles/cljs" "styles/cljc"]

     :figwheel {:on-jsload "feels.system/reset"}

     :compiler
     {
      :main cljs.user
      :asset-path "js/compiled/out"
      :output-to "resources/public/js/compiled/feels.js"
      :output-dir "resources/public/js/compiled/out"
      :source-map-timestamp true

      :foreign-libs [{:file "target/emoji-bundle.js"
                      :provides ["emoji"]
                      :module-type :commonjs}]
      :language-in :ecmascript6

      }}


    {:id "devcards"
     :source-paths  ["src/cljs" "test/cljs" "src/cljc" "test/cljc"
                     "styles/cljs" "styles/cljc"]
     :figwheel {:devcards true}
     :compiler
     {
      :main feels.devcards.core
      :asset-path
      "js/compiled/devcards_out"
      :output-to
      ;; matches http-server-root for figwheel
      "resources/devcards/js/compiled/feels_devcards.js"
      :output-dir
      "resources/devcards/js/compiled/devcards_out"
      :source-map-timestamp true

      :foreign-libs [{:file "target/emoji-bundle.js"
                      :provides ["emoji"]
                      :module-type :commonjs}]
      :language-in :ecmascript6

      }}


    {:id "test"
     :source-paths ["src/cljs" "test/cljs" "src/cljc" "test/cljc"]
     :compiler {:output-to "resources/public/js/compiled/testable.js"
                :main feels.test-runner
                :optimizations :none}}

    {:id "min"
     :source-paths ["src/cljs" "src/cljc"]
     :jar true
     :compiler {:main feels.system
                :output-to "resources/public/js/compiled/feels.js"
                :output-dir "target"
                :source-map-timestamp true
                :optimizations :advanced
                :pretty-print false}}
    ]}

  :figwheel {:css-dirs ["resources/public/css"]
             :server-logfile "log/figwheel.log"}

  :doo {:build "test"}

  :profiles
  {:dev
   {:dependencies [[figwheel "0.5.13"]
                   [figwheel-sidecar "0.5.13"]
                   [com.cemerick/piggieback "0.2.2"]
                   [org.clojure/tools.nrepl "0.2.13"]
                   [lein-doo "0.1.7"]
                   [reloaded.repl "0.2.3"]]

    :plugins [[lein-figwheel "0.5.13"]
              [lein-doo "0.1.7"]]

    :source-paths ["dev"]
    :repl-options {:nrepl-middleware
                   [cemerick.piggieback/wrap-cljs-repl]}
    }

   :uberjar
   {:source-paths ^:replace ["src/clj" "src/cljc"]
    :prep-tasks ["compile"
                 ["cljsbuild" "once" "min"]
                 ["run" "-m" "garden-watcher.main" "feels.styles"]]
    :hooks []
    :omit-source true
    :aot :all
    }
   }

  )
