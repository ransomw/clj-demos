(ns setsail.comp.dom-test
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require
   [cljs.core.async :refer [<! timeout]]
   [om.core :as om :include-macros true]
   [sablono.core :as html :refer-macros [html]]

   [rstyles-build.core :refer [garden->react]]
   [setsail.act :as act]
   [setsail.styles.core :as sty]
   ))

(defn db-records-test-md [thingone-names]
  [:section
   [:div
    {:style (garden->react sty/buttons-div-style)}
    [:button
     {:onClick #(act/create-thingone "nameless, nameless")
      } "Heyhey!"]
    [:button
     {:onClick #(act/create-thingone "O, lost!")
      } "Say, say.."]
    ]
   (if (not (= 0 (count thingone-names)))
     [:ul
      {:style (garden->react sty/msg-list-style)}
      (for [[react-key li-text]
            (map list (range (count thingone-names)) thingone-names)]
        [:li {:key react-key} li-text])
      ])
   ])

(defn vid-ref [owner video-el]
  (if (and (nil? (.-video owner)) (not (nil? video-el)))
    (do
      (set! (.-video owner) video-el)
      (-> video-el
          (.addEventListener
           "playing"
           (fn []
             (om/set-state! owner :vid-playing true)
             )))
      (-> video-el
          (.addEventListener
           "ended"
           (fn []
             (go
               (<! (timeout 1200))
               (set! (.-currentTime video-el) 5.5)
               (<! (timeout 750))
               (set! (.-currentTime video-el) 0)
               (om/set-state! owner :vid-playing false))
             )))
    )))

(defn vid-test-md [owner vid-url vid-playing]
  [:section
   (if vid-url
     [:div.video-container
      [:video
       {:src vid-url
        :ref (fn [video-el]
               (vid-ref owner video-el))
        :crossorigin "anonymous"}]
      (if (not vid-playing)
        [:div.video-overlay
         [:span.play
          {:onClick #(-> owner (.-video) (.play))
           :style (garden->react sty/play-style)
           } "play"]
         ])
      ]
     [:span "..loading video"])
   ])

(defn img-test-md [img-url anim-per]
  [:div
   {:style (garden->react (sty/img-test-el-style anim-per))}
   (if img-url
     [:img {:src img-url
            :style (garden->react sty/img-el-style)}]
     [:span "..loading img"])
   ])

(defn dom-test-view [{:keys [text vid-url img-url thingone-names]}
                     owner]
  (reify
      om/IInitState
    (init-state [_]
      {:tick 0
       :vid-playing false})
    om/IWillMount
    (will-mount [_]
      (go
        (while (not (nil? (om/get-state owner :tick)))
          (<! (timeout 150))
          (om/update-state!
           owner
           (fn [{:keys [tick] :as prev-state}]
             (merge prev-state
                    {:tick (mod (+ 1 tick) 100)})
             )))
        ))
    om/IWillUnmount
    (will-unmount [_]
      (om/set-state! owner :tick nil)
      )
    om/IRenderState
    (render-state [this {:keys [tick vid-playing]}]
      (html
       [:div
        [:header
         (img-test-md img-url tick)
         ]
        [:h2 text]
        [:section#interactive
         (db-records-test-md thingone-names)
         (vid-test-md owner vid-url vid-playing)
         ]
        [:footer
         [:span {:style {:text-decoration "underline"}} "is"]
         " it more "
         [:a {:href "http://openjdk.java.net/projects/icedtea/"}
          "iced tea"]
         " ye be needin"
         [:a {:href "mailto:auvergnerw@gmail.com"} "?"]
         ]
        ]
        ))))
