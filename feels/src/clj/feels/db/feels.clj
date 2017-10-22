(ns feels.db.feels
  (:require
   [clojure.set :refer [rename-keys]]
   [hugsql.core :as hugsql]
   [clj-time.coerce :refer [from-sql-time]]
   [clj-time.format :as tformat]
   [feels.db.feels-hug :as hug]
   ))

(hugsql/def-db-fns "feels/db/feels_schema.sql")
(hugsql/def-sqlvec-fns "feels/db/feels_schema.sql")

(def feels-keys
  (set [:happy :sleepy :grumpy]))

(def date-formatter
  (tformat/formatter "yyyy-MM-dd")
  )

(defn add-feels [db user-id feels]
  {:pre [(= (set (keys feels)) feels-keys)]}
  (-> (hug/add-feels db (merge {:user-id user-id} feels))
      first :id))

(defn timestamp-to-date [timestamp]
  (->> (from-sql-time timestamp)
       (tformat/unparse date-formatter)
       ))

(defn reshape-feels-from-db [feels-from-db]
  (if feels-from-db
    (merge
     (apply (partial dissoc feels-from-db) feels-keys)
     {:feels (zipmap feels-keys
                     (map #(get feels-from-db %) feels-keys))}
     )))

(defn get-feels [db user-id]
  (->> (hug/get-all-feels db {:user-id user-id})
       (map #(dissoc % :appuser_id))
       (map #(dissoc % :id))
       (map #(rename-keys % {:created_at :date}))
       (map #(update-in % [:date] timestamp-to-date))
       (map reshape-feels-from-db)
       ))

(defn get-today-feels [db user-id]
  (-> (hug/get-curr-feels db {:user-id user-id})
      first
      (dissoc :appuser_id)
      (dissoc :created_at)
      ))

(defn update-feels [db feels-id feels]
  {:pre [(= (set (keys feels)) feels-keys)]}
  (hug/update-feels db (merge {:feels-id feels-id} feels)))
