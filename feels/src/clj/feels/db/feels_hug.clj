(ns feels.db.feels-hug
  (:require
   [hugsql.core :as hugsql]
   ))

(hugsql/def-db-fns "feels/db/feels.sql")
(hugsql/def-sqlvec-fns "feels/db/feels.sql")
