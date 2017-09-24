(ns feels.config
  (:require
   [environ.core :refer [env]]
   [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
   [ring.middleware.gzip :refer [wrap-gzip]]
   [ring.middleware.logger :refer [wrap-with-logger]]
   [ring.middleware.session :refer [wrap-session]]
   ))

(defn config []
  {:http-port  (Integer. (or (env :port) 10555))
   :middleware [[wrap-defaults site-defaults]
                ;; wrap-with-logger
                wrap-gzip]
   ;; pretty-print format from clojure.jdbc docs
   :db-spec {:vendor "postgresql"
             :name "feels"
             :host "localhost"
             :user "sandy"
             :password "abc123"
             }
   })
