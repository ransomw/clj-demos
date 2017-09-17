(ns feels.test-runner
  (:require
   [doo.runner :refer-macros [doo-tests]]
   [feels.core-test]
   [feels.common-test]))

(enable-console-print!)

(doo-tests 'feels.core-test
           'feels.common-test)
