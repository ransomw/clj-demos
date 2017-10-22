(ns feels.devcards.calendar
  (:require-macros
   [devcards.core :as dc :refer
    [defcard defcard-doc noframe-doc deftest]]
   [cljs.test :refer [is testing]]
   )
  (:require
   [devcards.core]
   [cljs-time.core :as time]

   [feels.ui-comp.calendar :as cal]
   [feels.devcards.util :refer [rum-mount]]
   ))

(defcard-doc
  "like the calendar on a fridge")

(def this-month (time/first-day-of-the-month (time/today)))

(deftest cljs-time-tests
  (testing "checks on cljs-time api"
    (is (time/date? this-month))
    (is (time/date? (time/plus this-month (time/days 3))))
    ))

(defcard
  "**feels for a month**"
  (rum-mount cal/page)
  {:month this-month
   :feels-coll (map (fn [num-days]
                      {:date
                       (time/plus this-month (time/days num-days))
                       :feels
                       (zipmap [:happy :sleepy :grumpy]
                               (repeatedly 3 #(int (rand 11))))
                       })
                    (range 0 28 4))
   }
  {:inspect-data true}
  )
