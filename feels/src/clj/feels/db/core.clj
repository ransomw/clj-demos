
(ns feels.db.core
  (:require
   [hugsql.core :as hugsql]
   [hugsql.adapter.clojure-jdbc :as cj-adapter]

   [feels.db.user :as user]
   [feels.db.feels :as feels]
   ))

(defn app-init []
  (hugsql/set-adapter! (cj-adapter/hugsql-adapter-clojure-jdbc)))


(defn reset-db! [db]
  (feels/drop-table-feels (:connection db))
  (user/drop-table-user (:connection db))
  (user/create-table-user (:connection db))
  (feels/create-table-feels (:connection db))
  )

(defn add-user! [db name pass]
  (user/add-user (:connection db) name pass)
  )

(defn check-pass [db name pass]
  (user/check-pass (:connection db) name pass)
  )

(defn get-user [db name]
  (user/get-user (:connection db) name)
  )

;; (defn add-feels! [db user-id feels]
;;   (feels/add-feels (:connection db) user-id feels))

(defn get-feels [db user-id]
  (feels/get-feels (:connection db) user-id))

(defn get-today-feels [db user-id]
  (-> (feels/get-today-feels (:connection db) user-id)
      (dissoc :id)
      ))

(defn set-today-feels! [db user-id feels]
  (let [{feels-id :id
         } (feels/get-today-feels (:connection db) user-id)]
    (if feels-id
      (feels/update-feels (:connection db) feels-id feels)
      (feels/add-feels (:connection db) user-id feels))
    ))
