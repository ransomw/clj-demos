(ns feels.comm.core
  (:require-macros
   [cljs.core.async.macros :refer [go]]
   )
  (:require
   [cljs.core.async :refer [<!]]
   [citrus.core :as citrus]

   [feels.comm.user-feels :as user-feels]
   ))

(def fetch-user-feels user-feels/fetch-feels)
(def fetch-user-today-feels user-feels/fetch-today-feels)
(def set-user-today-feels user-feels/set-today-feels)

(defmulti comm-effects (fn [_ _ params] (:action params)))

(defmethod comm-effects :get-user-feels
  [r c {:keys [on-load on-err]}]
  (go
    (let [{:keys [res err]} (<! (user-feels/fetch-feels))]
      (if err
        (citrus/dispatch! r c on-err {:err err})
        (citrus/dispatch! r c on-load res))
      )))

(defmethod comm-effects :get-user-today-feels
  [r c {:keys [on-load on-err]}]
  (go
    (let [{:keys [res err]} (<! (user-feels/fetch-today-feels))]
      (if err
        (citrus/dispatch! r c on-err {:err err})
        (citrus/dispatch! r c on-load res))
      )))

(defmethod comm-effects :set-user-today-feels
  [r c {:keys [on-load on-err data]}]
  (go
    (let [{:keys [res err]} (<! (user-feels/set-today-feels data))]
      (if err
        (citrus/dispatch! r c on-err {:err err})
        (citrus/dispatch! r c on-load))
      )))
