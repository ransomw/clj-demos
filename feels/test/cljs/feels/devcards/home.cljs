(ns feels.devcards.home
  (:require-macros
   [devcards.core :as dc :refer
    [defcard defcard-doc noframe-doc deftest]]
   [cljs.test :refer [is testing]]
   )
  (:require
   [devcards.core]
   [citrus.core :as citrus]

   [feels.state :as state]
   [feels.ui-comp.home :as home]
   [feels.devcards.util :refer [rum-mount]]
   [feels.dat-checks :refer [example-feels]]
   ))

(def r state/reconciler)
(def st state/app-state)

(deftest reconciler-tests
  (testing "checks on the citrus reconciler"
    (citrus/broadcast-sync! r :init)
    (citrus/dispatch-sync! r :user-today-feels :load-ready
                           {:feels example-feels})
    (is (= example-feels
           (deref
            (citrus/subscription
             r [:user-today-feels :feels]))))
    ))

(defcard
  "**home component**"
  (rum-mount home/home :as-atom true)
  r
  {:inspect-data true}
  )
