(ns setsail.db.core
  (:require
   [setsail.db.thingone :as thingone]
   ))

(defn reset-db! [db]
  (thingone/drop-table (:connection db))
  (thingone/create-table (:connection db))
  )

;; returns number of rows inserted
(defn add-a-thingone! [db name]
  (thingone/add-a-thing (:connection db) name)
  )

(defn all-the-thingone-names [db]
  (thingone/all-the-names (:connection db))
  )
