(ns feels.e2e-runner-ctrl-user-feels
  (:require
   [doo.runner :refer-macros [doo-tests]]
   [feels.ctrl-user-feels-test]
   ))

(doo-tests
 'feels.ctrl-user-feels-test
 )
