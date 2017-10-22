(ns feels.core
  (:require
   [bidi.bidi :as bidi]
   [rum.core :as rum]
   [citrus.core :as citrus]

   [feels.state :refer [reconciler]]
   [feels.routes :refer [make-routes]]
   [feels.ui-comp.core :refer [root]]
   ))

(enable-console-print!)

(defn render []
  (rum/mount
   (root reconciler (make-routes))
   (. js/document (getElementById "app"))))
