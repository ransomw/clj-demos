(ns feels.routes-test
  (:require
   [clojure.test :refer :all]
   [clojure.string :refer [split]]
   [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
   [ring.middleware.session :refer [wrap-session]]
   [ring.mock.request :as mock]
   [hickory.core :as hickory]
   [hickory.select :as s]

   [feels.routes :as r]
   ))

(def ^{:dynamic true} *test-handler*)
(def ^{:dynamic true} *ring-session-id*)
(def ^{:dynamic true} *anti-forgery-token*)

(defn make-test-handler []
  (->
   (r/home-routes nil)
   (wrap-defaults
    site-defaults
    )
   ))

(defn routes-fixture [f]
  (binding [*test-handler* (make-test-handler)
            *ring-session-id* nil
            *anti-forgery-token* nil]
    (f)))

(use-fixtures :each routes-fixture)

(defn make-request [ req ]
  (let [
        res (*test-handler*
             (if *ring-session-id*
               (-> req
                   (mock/header "Cookie"
                                (str "ring-session=" *ring-session-id*))
                   )
               req))
        headers (:headers res)
        set-cookie-header (get headers "Set-Cookie")
        ring-session-id
        (if set-cookie-header
          (last (last (filter
                       (fn [l] (= "ring-session" (first l)))
                       (map #(split % #"=")
                            (split (first set-cookie-header) #";"))))))
        uri (:uri req)
        ]
    (if ring-session-id
      (set! *ring-session-id* ring-session-id))
    [req res]
    ))

(defn follow-redirects [[req res]]
  (let [location (get (:headers res) "Location")]
    (if location
      (follow-redirects (make-request (mock/request :get location)))
      [req res])
    ))

(deftest login-test
  (let [[req res] (make-request (mock/request :get "/"))]
    (is (= 302 (:status res)))
    )
  (let [[req res] (follow-redirects
                   (make-request (mock/request :get "/")))]
    (is (= "/login" (:uri req)))
    (is (= 200 (:status res)))
    (let [htree (-> res
                    :body
                    hickory/parse
                    hickory/as-hickory)
          token
          (->
           (s/select (s/child (s/tag :form)
                              (s/attr :type #(= % "hidden"))
                              ) htree)
           first :attrs :value)
          ]
      (set! *anti-forgery-token* token))
    )
  (let [init-req
        (-> (mock/request :post "/login")
            (mock/body
             {:__anti-forgery-token *anti-forgery-token*}))
        [req res] (follow-redirects (make-request init-req))
        ]
    (is (= "/" (:uri req)))
    (is (= 200 (:status res)))
    )
  (let [[req res] (make-request (mock/request :get "/"))]
    (is (= 200 (:status res)))
    )
  (let [[req res] (follow-redirects
                   (make-request (mock/request :get "/logout")))]
    (is (= 200 (:status res)))
    )
  (let [[req res] (make-request (mock/request :get "/"))]
    (is (= 302 (:status res)))
    )
  )
