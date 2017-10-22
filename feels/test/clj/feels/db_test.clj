(ns feels.db-test
  (:require
   [clojure.test :refer :all]
   [com.stuartsierra.component :as component]

   [feels.config :refer [config]]
   [feels.db.core :as db]
   [feels.db.component :refer [new-database]]
   ))

(def ^{:dynamic true} *db*)

(defn db-fixture [f]
  (binding [*db* (component/start
                  (new-database (:db-spec (config))))]
    (db/reset-db! *db*)
    (f)
    (component/stop *db*)
    ))

(use-fixtures :each db-fixture)

(deftest login-test
  (let [name "bob"
        pass "temp321!"]
    (is (nil? (db/get-user *db* name)))
    (is (= false (db/check-pass *db* name pass)))
    (is (integer? (db/add-user! *db* name pass)))
    (is (not (nil? (db/get-user *db* name))))
    (is (= true (db/check-pass *db* name pass)))
    (is (= false (db/check-pass *db* name (str pass "asdf"))))
    ))

(def example-feels
  {:happy 5 :sleepy 5 :grumpy 5})

(defn check-feels-from-db [feels-from-db]
  (is (= (-> feels-from-db keys set)
         (set [:feels :date])))
  (is (string? (:date feels-from-db)))
  (is (map? (:feels feels-from-db)))
  (is (= (-> feels-from-db :feels keys set)
         (-> example-feels keys set)))
  )

(deftest feels-test
  (let [user-id (db/add-user! *db* "bob" "temp321!")]
    (is (= 0 (count (db/get-feels *db* user-id))))
    (is (nil? (db/get-today-feels *db* user-id)))
    (is (integer? (db/set-today-feels! *db* user-id example-feels)))
    (let [all-feels (db/get-feels *db* user-id)]
      (is (= 1 (count all-feels)))
      (check-feels-from-db (first all-feels))
      )
    (is (= example-feels
           (db/get-today-feels *db* user-id)))
    (let [updated-example-feels (merge example-feels {:grumpy 1})]
      (is (integer?
           (db/set-today-feels! *db* user-id updated-example-feels)))
      (is (= 1 (count (db/get-feels *db* user-id))))
      (is (= updated-example-feels
             (db/get-today-feels *db* user-id)))
      )
    ))
