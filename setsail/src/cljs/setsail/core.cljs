(ns setsail.core
  (:require-macros [cljs.core.async.macros :refer [go]])

  (:require
   [om.core :as om :include-macros true]

   [setsail.comp.root :as comp-root]
   [setsail.state :as st]
   [setsail.act :as act]
   ))

(enable-console-print!)

(defn init-demo []
  (act/fetch-text)
  (act/fetch-vid)
  (act/fetch-img)
  (act/fetch-thingones)
  )

(defn demo-main []
  (om/root
   comp-root/root-component
   st/app-state
   {:target (js/document.getElementById "app")}))

(defn run-demo []
  (init-demo)
  (demo-main))

(run-demo)
