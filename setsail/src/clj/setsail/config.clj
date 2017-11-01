(ns setsail.config
  (:require
   [environ.core :refer [env]]
   [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
   [ring.middleware.gzip :refer [wrap-gzip]]
   [ring.middleware.logger :refer [wrap-with-logger]]
   [ring.middleware.session :refer [wrap-session]]
   [ring.middleware.format :refer [wrap-restful-format]]
   [ring.middleware.cors :refer [wrap-cors]]
   ))

(defn config [& {:keys [doo-test]}]
  {:http-port (Integer. (or (env :port) 10555))
   :middleware
   [[wrap-defaults api-defaults]
    [wrap-restful-format :format [:edn]]
    ;; wrap-with-logger
    wrap-gzip
    ]
   :db-spec
   {:vendor "postgresql"
    :name "setsail"
    :host "localhost"
    :user "sandy"
    :password "abc123"
    }
   })
