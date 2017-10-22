(ns feels.comm.user-feels
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require
   [cljs-http.client :as http]
   [cljs.core.async :refer [chan <! >! close!]]
   [cljs-time.core :as time]
   [cljs-time.format :as time-format]

   [feels.config :as config]
   ))

(def base-url (str config/base-url "/api"))

(def date-formatter (time-format/formatter "yyyy-MM-dd"))

(defn update-feels-from-coll [feels]
  (update-in feels [:date]
             (partial time-format/parse-local-date date-formatter))
             )

(defn fetch-feels []
  (let [res-chan (chan)]
    (go (let [response (<! (http/get
                            (str base-url "/feels")))]
          (if (:success response)
            (>! res-chan {:res
                          (map update-feels-from-coll
                               (:body response))
                          })
            (>! res-chan {:err (:body response)}))
          (close! res-chan)
          ))
    res-chan))

(defn fetch-today-feels []
  (let [res-chan (chan)]
    (go (let [response (<! (http/get
                            (str base-url "/feels/today")))]
          (if (:success response)
            (>! res-chan {:res (:body response)})
            (>! res-chan {:err (:body response)}))
          (close! res-chan)
          ))
    res-chan))

(defn set-today-feels [feels]
  {:pre [(map? feels)
         (= #{:happy :sleepy :grumpy}
            (-> feels keys set))]}
  (let [res-chan (chan)]
    (go (let [response (<! (http/post
                            (str base-url "/feels/today")
                            {:edn-params {:feels feels}}))]
          (if (:success response)
            (>! res-chan {:res (:body response)})
            (>! res-chan {:err (:body response)}))
          (close! res-chan)
          ))
    res-chan))
