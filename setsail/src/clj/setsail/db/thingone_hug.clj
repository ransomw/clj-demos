(ns setsail.db.thingone-hug
  (:require
   [hugsql.core :as hugsql]
   ))

(hugsql/def-db-fns "setsail/db/thingone.sql")
(hugsql/def-sqlvec-fns "setsail/db/thingone.sql")
