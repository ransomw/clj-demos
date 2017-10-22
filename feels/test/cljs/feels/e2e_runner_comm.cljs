(ns feels.e2e-runner-comm
  (:require
   [doo.runner :refer-macros [doo-tests]]
   [feels.comm-test]
   ))

(doo-tests
 'feels.comm-test
 )
