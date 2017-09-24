(ns feels.ui-comp.calendar
  (:require
   [cljs-time.core :as time]
   [cljs-time.predicates :as timep]
   [rum.core :as rum]

   [rstyles-build.core :refer [garden->react]]
   [feels.styles.core :as sty]
   ;; js lib
   [emoji]
   ))


(def month-preds
  [
   timep/january?
   timep/february?
   timep/march?
   timep/april?
   timep/may?
   timep/june?
   timep/july?
   timep/august?
   timep/september?
   timep/october?
   timep/november?
   timep/december?
   ])

(def month-strs
  [
   "January"
   "February"
   "March"
   "April"
   "May"
   "June"
   "July"
   "August"
   "September"
   "October"
   "November"
   "December"
   ])

(def day-preds
  [
   timep/sunday?
   timep/monday?
   timep/tuesday?
   timep/wednesday?
   timep/thursday?
   timep/friday?
   timep/saturday?
   ])

(def day-strs
  [
   "Su"
   "Mo"
   "Tu"
   "We"
   "Th"
   "Fr"
   "Sa"
   ])

(defn get-month-str [date-inst]
  (get
   (zipmap (map #(% date-inst) month-preds)
           month-strs)
   true))

(defn keyed-list [some-list]
  (map list (range (count some-list)) some-list))

(defn padded-days [month]
  (let [tot-days (time/number-of-days-in-the-month month)
        first-day (time/first-day-of-the-month month)
        last-day (time/last-day-of-the-month month)
        padding-start
        (take-while not (map #(% first-day) day-preds))
        padding-end
        (rest (drop-while not (map #(% last-day) day-preds)))
        ]
    (concat
     padding-start
     (map
      (fn [num-days]
        (time/plus first-day (time/days num-days)))
      (range tot-days))
     padding-end)
    ))

(defn ref-day-canvas [feel el]
  (if el
    (do
      (let [cdim (-> el .-offsetWidth)]
        (set! (-> el .-offsetHeight) cdim)
        (set! (-> el .-width) cdim)
        (set! (-> el .-height) cdim))
      (if feel
        (emoji/draw (clj->js feel) el))
      )))

(rum/defcs cal-day [_ {:keys [day feel rkey]}]
  [:div {:key rkey
         :style (garden->react sty/cal-day-container-style)}
   (if day
     [:span
      {:style (garden->react sty/cal-day-date-style)}
      (str (time/day day))]
     [:div {:style (garden->react sty/cal-day-grey-overlay)}]
     )
   (if feel
     [:canvas
      {:style (garden->react sty/cal-day-canvas-style)
       :ref (partial ref-day-canvas feel)
       }]
     (if day
       [:div
        {:style (garden->react sty/cal-day-passed-style)
         }]))
   ])

(rum/defc page < rum/static [{:keys [month feels]}]
  [:div
   [:h3 (str (get-month-str month) " " (str (time/year month)))]
   [:div
    [:div {:key "day-labels"
           :style (garden->react sty/cal-row-header-style)}
     (for [[key day-str] (keyed-list day-strs)]
       [:span {:key key} day-str])
     ]
    (for [[key some-week] (keyed-list
                           (partition 7 (padded-days month)))]
      [:div {:key key
             :style (garden->react sty/cal-row-body-style)}
       (for [[key some-day] (keyed-list some-week)]
         (cal-day {:rkey key :day some-day
                   :feel
                   (->> feels
                        (filter
                         (fn [{day :day}]
                           (time/equal? day some-day)
                           ))
                        first :feel)
                   }))
       ])
    ]
   ])
