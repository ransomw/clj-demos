(ns setsail.db.thingone

  (:require
   [hugsql.core :as hugsql]
   [honeysql.core :as sql]
   [honeysql.helpers :refer :all]
   ))

(hugsql/def-db-fns "setsail/db/thingone.sql")
(hugsql/def-sqlvec-fns "setsail/db/thingone.sql")

(defn add-a-thing [name]
  (-> (insert-into :thingone)
      (values [{:nameone name}])
      sql/format)
  )

(defn all-the-names []
  (sql/format
   {
    :select [:nameone]
    :from [:thingone]
    }))
