(ns feels.routes-test
  (:require
   [clojure.test :refer :all]
   [clojure.string :refer [split]]
   [clojure.edn :as edn]
   [com.stuartsierra.component :as component]
   [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
   [ring.middleware.session :refer [wrap-session]]
   [ring.mock.request :as mock]
   [hickory.core :as hickory]
   [hickory.select :as s]

   [feels.db.core :refer [reset-db!]]
   [feels.db.component :refer [new-database]]
   [feels.application]
   [feels.config :refer [config]]
   [feels.routes :as r]
   ))

(def ^{:dynamic true} *db*)
(def ^{:dynamic true} *test-handler*)
(def ^{:dynamic true} *ring-session-id*)
(def ^{:dynamic true} *anti-forgery-token*)

(defn make-test-handler []
  (->
   (r/home-routes {:db *db*})
   (wrap-defaults
    (assoc-in site-defaults [:security :anti-forgery] false)
    )
   )
  )


(defn routes-fixture [f]
  (binding [*db* (component/start
                  (new-database (:db-spec (config))))]
    (reset-db! *db*)
    (binding [*test-handler* (make-test-handler)
              *ring-session-id* nil
              *anti-forgery-token* nil]
      (f))
    (component/stop *db*)
    ))

(use-fixtures :each routes-fixture)

;;; ring-mock helpers

(defn make-request [ req ]
  (let [
        res (*test-handler*
             (if *ring-session-id*
               (-> req
                   (mock/header
                    "Cookie"
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

(defn mock-body-anti-forgery [request body-value]
  {:pre [(map? body-value)]}
  (->> (merge
        body-value
        (if *anti-forgery-token*
          {:__anti-forgery-token *anti-forgery-token*}
          {}))
       (mock/body request)
       ))

(defn mock-body-edn [request body-value]
  {:pre [(map? body-value)]}
  (-> request
      (mock/header :content-type "application/edn")
      ;; without casting to a string, ring-mock
      ;; will set content-type to form data
      (mock/body (str body-value))
      ))

(defn set-anti-forgery-login []
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
    ))

(deftest login-test
  (let [name "bob"
        pass "temp321"]
    (let [[req res] (make-request (mock/request :get "/"))]
      (is (= 302 (:status res)))
      )
    (set-anti-forgery-login)
    (let [init-req
          (-> (mock/request :post "/login")
              (mock-body-anti-forgery {:username name :password pass}))
          [req res] (follow-redirects (make-request init-req))
          ]
      (is (= 401 (:status res)))
      )
    (let [init-req
          (-> (mock/request :post "/register")
              (mock-body-anti-forgery {:username name :password pass}))
          [req res] (follow-redirects (make-request init-req))
          ]
      (is (= 200 (:status res)))
      (is (= "/" (:uri req)))
      (is (not (nil? *anti-forgery-token*)))
      (is (not (nil? *ring-session-id*)))
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
    (let [init-req
          (-> (mock/request :post "/login")
              (mock-body-anti-forgery {:username name :password pass}))
          [req res] (follow-redirects (make-request init-req))
          ]
      (is (= 200 (:status res)))
      (is (= "/" (:uri req)))
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
    ))

(def example-feels
  {:happy 5 :sleepy 5 :grumpy 5})

(defn check-feels-from-coll [feels-from-coll]
  (is (= (-> feels-from-coll keys set)
         (set [:feels :date])))
  (is (string? (:date feels-from-coll)))
  (is (map? (:feels feels-from-coll)))
  (is (= (-> feels-from-coll :feels keys set)
         (-> example-feels keys set)))
  )

(deftest feels-test
  (let [[req res] (make-request (mock/request :get "/"))]
    (is (= 302 (:status res)))
    )
  (set-anti-forgery-login)
  (-> (mock/request :post "/register")
      (mock-body-anti-forgery
       {:username "bob" :password "temp321"})
      make-request follow-redirects)
  (is (not (nil? *ring-session-id*)))
  (let [[_ res-feels] (-> (mock/request :get "/api/feels")
                          make-request)]
    (is (not (nil? res-feels)))
    (is (= (:status res-feels) 200))
    (is (= (-> res-feels :body edn/read-string count) 0))
    )
  (let [[_ res-today-feels] (-> (mock/request :get "/api/feels/today")
                                make-request)
        res-today-feels-edn (-> res-today-feels :body edn/read-string)]
    (is (not (nil? res-today-feels)))
    (is (= (:status res-today-feels) 200))
    (is (map? res-today-feels-edn))
    (is (= #{:feels} (-> res-today-feels-edn keys set)))
    (is (nil? (:feels res-today-feels-edn)))
    )
  (let [[_ res-set-feels
         ] (-> (mock/request :post "/api/feels/today")
               (mock-body-edn {:feels example-feels})
               make-request)]
    (is (not (nil? res-set-feels)))
    (is (= (:status res-set-feels) 200))
    )
  (let [[_ res-feels] (-> (mock/request :get "/api/feels")
                          make-request)]
    (is (not (nil? res-feels)))
    (is (= (:status res-feels) 200))
    (is (= (-> res-feels :body edn/read-string count) 1))
    (check-feels-from-coll (-> res-feels :body edn/read-string first))
    )
  (let [[_ res-today-feels] (-> (mock/request :get "/api/feels/today")
                                make-request)]
    (is (not (nil? res-today-feels)))
    (is (= (:status res-today-feels) 200))
    (is (= (-> res-today-feels :body edn/read-string :feels)
           example-feels))
    )
  (let [updated-example-feels (merge example-feels {:grumpy 1})
        [_ res-set-feels
         ] (-> (mock/request :post "/api/feels/today")
               (mock-body-edn {:feels updated-example-feels})
               make-request)
        [_ res-today-feels] (-> (mock/request :get "/api/feels/today")
                                make-request)]
    (is (= (-> res-today-feels :body edn/read-string :feels)
           updated-example-feels))
    )
  )
