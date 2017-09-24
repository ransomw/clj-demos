(ns feels.routes
  (:require
   [bidi.bidi :as bidi]
   [accountant.core :as accountant]

   [feels.state :refer [curr-route]]
   ))

(defn make-routes []
  ["/"
   {"" :home
    "log" :log
    }
   ])

(defn init-routes []
  (let [app-routes (make-routes)]
    (accountant/configure-navigation!
     {:nav-handler
      (fn [path]
        (println "in nav-handler, path:" path)
        (reset!
         curr-route
         (bidi/match-route app-routes path)
         )
        )
      :path-exists?
      (fn [path]
        (boolean (bidi/match-route app-routes path))
        )})
    ))
