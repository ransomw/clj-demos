(ns feels.db.user-hug
  (:require
   [hugsql.core :as hugsql]
   ))

(hugsql/def-db-fns "feels/db/user.sql")
(hugsql/def-sqlvec-fns "feels/db/user.sql")
