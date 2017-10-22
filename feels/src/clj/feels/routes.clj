(ns feels.routes
  (:require
   [clojure.java.io :as io]
   [compojure.core :refer [ANY GET PUT POST DELETE routes context]]
   [compojure.route :refer [resources]]
   [ring.util.response :refer [response redirect status]]
   [ring.middleware.anti-forgery
    :refer [wrap-anti-forgery *anti-forgery-token*]]
   [ring.middleware.format :refer [wrap-restful-format]]

   [feels.db.core :as db]
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

(defn auth-routes [{db :db}]
  (routes
   (context
    "/" {session :session}
    (GET
     "/login" _
     (-> (make-login-page-str *anti-forgery-token*)
         response
         (assoc :headers html-headers)))
    (POST
     "/login" [username password]
     (let [user (db/get-user db username)]
       (if (and user
                (db/check-pass db username password))
         (let [user-id (:id user)
               login-session (assoc session :user-id user-id)]
           (-> (redirect "/")
               (assoc :session login-session)))
         (-> (make-login-page-str *anti-forgery-token*)
             response
             (status 401)
             (assoc :headers html-headers))
         )))
    (POST
     "/register" [username password]
     (let [user-id (db/add-user! db username password)]
       (if user-id
         (let [login-session (assoc session :user-id user-id)]
           (-> (redirect "/")
               (assoc :session login-session)))
         (-> (make-login-page-str *anti-forgery-token*)
             response
             (status 500)
             (assoc :headers html-headers))
         )))
    (GET
     "/logout" _
     (let [session (assoc session :user-id nil)]
       (-> (redirect "/")
           (assoc :session session))))
    )))

(defn make-edn-resp
  ([data] (make-edn-resp data 200))
  ([data status]
   {:status status
    :headers {"Content-Type" "application/edn"}
    :body (pr-str data)}))

(defn api-routes [{db :db}]
  (routes
   (context
    "/" {{user-id :user-id} :session}
    (GET
     "/feels" _
     (-> (db/get-feels db user-id)
         make-edn-resp))
    (GET
     "/feels/today" _
     (let [today-feels (db/get-today-feels db user-id)]
       (-> {:feels today-feels}
           make-edn-resp)))
    (POST
     "/feels/today" [feels]
     (if (integer? (db/set-today-feels! db user-id feels))
       (make-edn-resp feels)
       (make-edn-resp feels 500)
       ))
    )))

(defn home-routes [{db :db :as endpoint}]
  (routes
   (context
    "/" {{:keys [user-id]} :session}
    (if user-id
      (logged-in-routes)
      (GET
       "/" _
       (redirect "/login"))))
   (context
    "/api" {{:keys [user-id]} :session}
    (if user-id
      (-> (api-routes {:db db})
          (wrap-restful-format :format [:edn])
          )
      (GET
       "/" _
       (redirect "/login"))
      ))
   ;; must follow "/api" endpoints to prevent
   ;; anti forgery middleware from kicking in
   ;; .. perhaps if auth routes are in a different context than "/"?
   (-> (auth-routes {:db db})
       wrap-anti-forgery)
   (resources "/")
   ))
