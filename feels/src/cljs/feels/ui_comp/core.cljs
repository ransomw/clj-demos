(ns feels.ui-comp.core
  (:require
   [bidi.bidi :as bidi]
   [rum.core :as rum]
   [citrus.core :as citrus]

   [feels.ui-comp.home :refer [home]]
   [feels.ui-comp.log :refer [log]]
   ))

(rum/defc navigation < rum/static [routes]
  (let [path-for (partial bidi/path-for routes)]
    [:nav
     [:a {:href (path-for :home)} "home"]
     [:a {:href (path-for :log)} "log"]
     ]))

(rum/defc root < rum/reactive [r routes]
  [:div
   [:h1 "feels"]
   (navigation routes)
   (let [{route :handler params :route-params
          } (rum/react (citrus/subscription r [:router]))]
     ((get {:home home
            :log log
            } route) r)
     )
   ])
