(ns feels.routes
  (:require
   [clojure.java.io :as io]
   [compojure.core :refer [ANY GET PUT POST DELETE routes context]]
   [compojure.route :refer [resources]]
   [ring.util.response :refer [response redirect]]
   [ring.middleware.anti-forgery :refer [*anti-forgery-token*]]

   [feels.views.login :refer [make-login-page-str]]
   ))

(defonce html-headers
  {"Content-Type" "text/html; charset=utf-8"})

;; since *out* may not be set appropriately
(defn route-print [something]
  (.println System/out (str something)))

(defn logged-in-routes []
  (routes
   (GET
    "/" _
    (-> "public/index.html"
        io/resource
        io/input-stream
        response
        (assoc :headers html-headers)))
   ))

(defn home-routes [endpoint]
  (routes
   (context
    "/" {{:keys [user-id]} :session}
    (if user-id
      (logged-in-routes)
      (GET
       "/" _
       (redirect "/login"))))
   (GET
    "/login" {{:keys [user-id] :as session} :session}
      (-> (make-login-page-str *anti-forgery-token*)
          response
          (assoc :headers html-headers)))
   (POST
    "/login" {session :session}
    (let [session (assoc session :user-id 1)]
      (-> (redirect "/")
          (assoc :session session))))
   (GET
    "/logout" {session :session}
    (let [session (assoc session :user-id nil)]
      (-> (redirect "/")
          (assoc :session session))))
   (resources "/")
   ))
