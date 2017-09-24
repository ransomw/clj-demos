(ns feels.core
  (:require
   [bidi.bidi :as bidi]
   [rum.core :as rum]

   [feels.state :refer [app-state curr-route]]
   [feels.routes :refer [make-routes]]
   ))

(enable-console-print!)

(rum/defc navigation < rum/static []
  (let [path-for (partial bidi/path-for (make-routes))]
    [:nav
     [:a {:href (path-for :home)} "home"]
     [:a {:href (path-for :log)} "log"]
     ]))

(rum/defc log < rum/static []
  [:div
   [:span "log"]
   ])

(rum/defc home < rum/static []
  [:div
   [:span "hoooome"]
   ])

(rum/defc greeting < rum/reactive []
  [:div
   [:h1 "feels"]
   (navigation)
   [:h3 (:text (rum/react app-state))]
   ((get {:home home
          :log log
          } (:handler (rum/react curr-route))))
   ])


(defn render []
  (rum/mount (greeting) (. js/document (getElementById "app"))))
