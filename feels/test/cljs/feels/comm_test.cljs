(ns feels.comm-test
  (:require-macros
   [cljs.test :refer [is deftest testing async]]
   [cljs.core.async.macros :refer [go]]
   )
  (:require
   [cljs.test]
   [cljs.core.async :refer [chan <! >! close! timeout]]
   [cljs-http.client :as http]

   [feels.comm.core :as comm]
   [feels.dat-checks
    :refer [check-feels-from-coll
            example-feels
            ]]
   ))

(deftest user-feels-test
  (async
   done
   (go
     (let [{:keys [res err]} (<! (comm/fetch-user-feels))]
       (is (nil? err))
       (is (seq? res))
       (is (= 0 (count res)))
       )
     (let [{:keys [res err]} (<! (comm/fetch-user-today-feels))]
       (is (nil? err))
       (is (map? res))
       (is (= #{:feels} (-> res keys set)))
       )
     (let [{:keys [res err]} (<! (comm/set-user-today-feels
                                  example-feels))]
       (is (nil? err))
       (is (not (nil? res)))
       )
     (let [{:keys [res err]} (<! (comm/fetch-user-feels))]
       (is (nil? err))
       (is (seq? res))
       (is (= 1 (count res)))
       (check-feels-from-coll (first res))
       (is (= example-feels (-> res first :feels)))
       )
     (let [{:keys [res err]} (<! (comm/fetch-user-today-feels))]
       (is (nil? err))
       (is (= (:feels res) example-feels))
       )
     (done))))
