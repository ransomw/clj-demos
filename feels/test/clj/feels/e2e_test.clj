(ns feels.e2e-test
  (:require
   [clojure.string :as s]
   [clojure.test :refer :all]
   [com.stuartsierra.component :as component]
   [doo.core :as doo]
   [cljs.build.api :as cljs]

   [feels.application]
   [feels.config :refer [config]]
   [feels.db.core :as db :refer [reset-db! add-user!]]
   ))

(defn test-system []
  (feels.application/app-system (config :doo-test true))
  )

(def ^{:dynamic true} *db*)
(def ^{:dynamic true} *user-id*)

(use-fixtures :each
  (fn [f]
    (let [system (component/start (test-system))]
      (binding [*db* (:db system)]
        (reset-db! *db*)
        (binding [*user-id* (add-user! *db* "bob" "temp321")]
          (f)
          (component/stop system))
      ))))


(defn run-doo-phantom [cljs-runner-main]
  (let [compiler-opts {:main cljs-runner-main
                       :output-to "out/e2e_test.js"
                       :output-dir "out"
                       :asset-path "out"
                       :optimizations :none

                       :foreign-libs [{:file "target/emoji-bundle.js"
                                       :provides ["emoji"]
                                       :module-type :commonjs}]
                       :language-in :ecmascript6
                       }
        phantom-path (s/join " "
                             [
                              "phantomjs"
                              ;; _supposedly_ enables CORS
                              "--web-security=false"
                              ;; "--debug=true"
                              "--local-to-remote-url-access=true"
                              ])]
    ;; Compile the ClojureScript tests
    (cljs/build (apply cljs/inputs ["src/cljs"
                                    "src/cljc"
                                    "test/cljs"
                                    "env/test/cljs"
                                    ])
                compiler-opts)
    ;; Run the ClojureScript tests and check the result
    (:exit (doo/run-script
            :phantom
            compiler-opts
            {:paths {:phantom phantom-path} :debug true})
           )
    ))

(deftest e2e-suite-comm
  (is (= 1 *user-id*))
  (is (zero? (run-doo-phantom 'feels.e2e-runner-comm))))

(def example-feels
  {:happy 5 :sleepy 5 :grumpy 5})

(deftest e2e-suite-ctrl-user-feels
  (is (= 1 *user-id*))
  (is (integer? (db/set-today-feels! *db* *user-id* example-feels)))
  (is (zero? (run-doo-phantom 'feels.e2e-runner-ctrl-user-feels))))
