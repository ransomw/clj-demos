(ns feels.styles.core
  (:require
   [clojure.string :as s]
  ))

(def cal-row-common-style
  {:display "flex"
   :flex-direction "row"
   :justify-content "space-between"
   })

(def cal-row-header-style
  (merge
   cal-row-common-style
   {
    }))

(def cal-row-body-style
  (merge
   cal-row-common-style
   {
    }))

(def cal-day-container-style
  {:position "relative"
   :flex-grow "1"
   :flex-basis "0"
   })

(def cal-day-content-style
  {:position "relative"
   :width "100%"
   })

(def cal-day-canvas-style
  (merge
   cal-day-content-style
   {
    }))

(def cal-day-passed-style
  (merge
   cal-day-content-style
   {:background "darkseagreen"
    :height "100%"
    }))

(def cal-day-date-style
  {:position "absolute"
   :top "10%"
   :left "10%"
   :z-index "1"
   })

(def cal-day-grey-overlay
  {
   :position "absolute"
   :background "grey"
   :top "0"
   :left "0"
   :right "0"
   :bottom "0"
   })
