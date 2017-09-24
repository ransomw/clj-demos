(ns feels.state
  (:require
   [rum.core :refer [cursor-in]]
   ))

(defonce app-state
  (atom {:text "hey there"
         :route {:handler :home}
         }))

(defonce curr-route (cursor-in app-state [:route]))
