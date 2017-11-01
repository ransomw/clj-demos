(ns setsail.db.thingone
  (:require
   [hugsql.core :as hugsql]

   [setsail.db.thingone-hug :as hug]
   ))

(hugsql/def-db-fns "setsail/db/thingone_schema.sql")
(hugsql/def-sqlvec-fns "setsail/db/thingone_schema.sql")

(defn add-a-thing [db name]
  (-> (hug/add-a-thing db {:nameone name})
      first :id)
  )

(defn all-the-names [db]
  (map :nameone (hug/all-the-names db))
  )
