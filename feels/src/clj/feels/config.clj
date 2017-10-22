(ns feels.config
  (:require
   [environ.core :refer [env]]
   [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
   [ring.middleware.gzip :refer [wrap-gzip]]
   [ring.middleware.logger :refer [wrap-with-logger]]
   [ring.middleware.session :refer [wrap-session]]
   [ring.middleware.cors :refer [wrap-cors]]
   ))

(defn wrap-login-sess [handler]
  (let [user-id 1]
    (fn [request]
      (let [{sess :session} request
            login-sess (assoc sess :user-id user-id)]
        (handler (assoc request :session login-sess))
        ))
    ))

(defn config [& {:keys [doo-test]}]
  {:http-port  (Integer. (or (env :port) 10555))
   :middleware (->> [
                     (if doo-test wrap-login-sess)
                     [wrap-defaults
                      (-> site-defaults
                          (assoc-in [:security :anti-forgery] false)
                          )]
                     ;; wrap-with-logger
                     (if (not doo-test) wrap-gzip)
                     (if doo-test wrap-cors)
                     ]
                    (filter identity))
   ;; pretty-print format from clojure.jdbc docs
   :db-spec {:vendor "postgresql"
             :name "feels"
             :host "localhost"
             :user "sandy"
             :password "abc123"
             }
   })
