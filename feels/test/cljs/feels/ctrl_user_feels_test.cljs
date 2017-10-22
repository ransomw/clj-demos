(ns feels.ctrl-user-feels-test
  (:require-macros
   [cljs.test :refer [is deftest testing async]]
   [cljs.core.async.macros :refer [go]]
   )
  (:require
   [cljs.test]
   [clojure.set :as set]
   [cljs.core.async :refer [chan <! >! close! timeout]]
   [citrus.core :as citrus]

   [feels.state :as state]
   [feels.controllers.user-today-feels :as user-today-feels]
   [feels.controllers.user-feels :as user-feels]
   [feels.dat-checks
    :refer [check-feels-from-coll
            example-feels
            ]]
   ))

(def r state/reconciler)
(def st state/app-state)

(def comm-timeout 500)

(deftest user-feels-state-test
  (is (= #{} (-> st deref keys set (set/difference #{:text}))))
  (citrus/broadcast-sync! r :init)
  (is (seq? (-> st deref :user-feels :feels)))
  (is (map? (-> st deref :user-today-feels)))
  (is (contains? (-> st deref :user-today-feels keys set) :feels))
  (is (nil? (-> st deref :user-today-feels :feels)))
  (async
   done
   (go
     (citrus/dispatch-sync! r :user-feels :load)
     (is (-> st deref :user-feels :loading?))
     (<! (timeout comm-timeout))
     (is (not (-> st deref :user-feels :loading?)))
     (is (seq? (-> st deref :user-feels :feels)))
     (is (= 1 (-> st deref :user-feels :feels count)))
     (check-feels-from-coll (-> st deref :user-feels :feels first))
     (is (nil? (-> st deref :user-today-feels :feels)))
     (citrus/dispatch-sync! r :user-today-feels :load)
     (is (-> st deref :user-today-feels :loading?))
     (<! (timeout comm-timeout))
     (is (not (-> st deref :user-today-feels :loading?)))
     (is (map? (-> st deref :user-today-feels :feels)))
     (is (= (-> example-feels keys set)
            (-> st deref :user-today-feels :feels keys set)))
     (citrus/dispatch-sync! r :user-today-feels :set example-feels)
     (is (-> st deref :user-today-feels :loading?))
     (<! (timeout comm-timeout))
     (is (not (-> st deref :user-today-feels :loading?)))
     (is (= example-feels
            (-> st deref :user-today-feels :feels)))
     (done)))
  )
