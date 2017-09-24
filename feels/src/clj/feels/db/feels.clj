(ns feels.db.feels
  (:require
   [hugsql.core :as hugsql]
   [feels.db.feels-hug :as hug]
   ))

(hugsql/def-db-fns "feels/db/feels_schema.sql")
(hugsql/def-sqlvec-fns "feels/db/feels_schema.sql")

(defn add-feels [db user-id feels]
  {:pre [(= (set (keys feels))
            (set [:happy :sleepy :grumpy]))]}
  (hug/add-feels
   db
   (merge {:user-id user-id}
          feels)))

(defn get-feels [db user-id]
  (hug/get-all-feels
   db
   {:user-id user-id}))
