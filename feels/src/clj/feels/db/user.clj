(ns feels.db.user
  (:require
   [hugsql.core :as hugsql]
   [feels.db.user-hug :as hug]
   ))

(hugsql/def-db-fns "feels/db/user_schema.sql")
(hugsql/def-sqlvec-fns "feels/db/user_schema.sql")

(defn add-user [db name pass]
  (:id (first
        (hug/add-user db {:username name :password pass}))))

(defn get-user-internal [db name]
  (let [rows-res (hug/get-user
                  db
                  {:username name})
        ]
    (if (= 1 (count rows-res))
      (first rows-res))
    ))

(defn check-pass [db name pass]
  (= pass (-> (get-user-internal db name)
              (get :password)
              )))

(defn get-user [db name]
  (-> (get-user-internal db name)
      (dissoc :password)
      ))
