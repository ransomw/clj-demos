(ns feels.dat-checks
  (:require-macros
   [cljs.test :refer [is]]
   )
  (:require
   [cljs.test]
   [cljs-time.core :as time]
   ))

(def example-feels
  {:happy 5 :sleepy 5 :grumpy 3})

(defn check-feels-from-coll [feels-from-coll]
  (is (= (-> feels-from-coll keys set)
         (set [:feels :date])))
  (is (time/date? (:date feels-from-coll)))
  (is (map? (:feels feels-from-coll)))
  (is (= (-> feels-from-coll :feels keys set)
         (-> example-feels keys set)))
  )
