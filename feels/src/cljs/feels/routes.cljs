(ns feels.routes
  (:require
   [bidi.bidi :as bidi]
   [accountant.core :as accountant]
   ))

(defn make-routes []
  ["/"
   {"" :home
    "log" :log
    }
   ])

(defmulti control (fn [action _ _] action))

(defmethod control :init [_ [route] _]
  {:state (bidi/match-route (make-routes) "/")})

(defmethod control :push [_ [route] _]
  {:state route})

(defn start-router! [on-set-page]
  (let [routes (make-routes)]
    (accountant/configure-navigation!
     {:nav-handler
      (fn [path]
        (println "in nav-handler, path:" path)
        (on-set-page
         (bidi/match-route routes path)
         )
        )
      :path-exists?
      (fn [path]
        (boolean (bidi/match-route routes path))
        )}
     )))
