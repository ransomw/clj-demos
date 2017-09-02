(ns setsail.comp.root
  (:require
   [om.core :as om :include-macros true]
   [sablono.core :as html :refer-macros [html]]

   [setsail.styles.fonts :refer [font-faces]]
   [setsail.comp.dom-test :refer [dom-test-view]]
   ))

(defn root-component [app owner]
  (reify
    om/IRender
    (render [_]
      (html
       [:div
        [:div {:style {:opacity "0"}}
         ;; preload fonts to avoid flicker
         (for [font-face (vals font-faces)]
           [:span
            {:style {:font-family (str "'" font-face "'")}}
            ])
         ]
        (om/build
         dom-test-view
         {:text (:text app)
          :vid-url (:vid-url app)
          :img-url (:img-url app)
          :thingone-names (:thingone-names app)
          }
         )])
      )))
