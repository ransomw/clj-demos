(ns garden-build.core
  (:require
   [hawk.core :as hawk]
   [garden.core :as g]
   [garden.units :as u]
   [garden.selectors :as s]
   [garden.stylesheet :as stylesheet]
  ))

;; always build one stylesheet from the list named 'main
;; ... this could be generalized.  the conventions in garden-watcher
;;     might not always be a good fit.  unclear at this point what
;;     the desired convention/configuration split might be.
(defn build-once [style-ns garden-opts]
  (require style-ns :reload-all)
  (g/css
   garden-opts
   (var-get (get (ns-publics style-ns) 'main))
   )
  )

(defn start-build! [{:keys [source-paths style-ns]} garden-opts]
  (let [build-once (partial build-once style-ns garden-opts)]
    (build-once)
    ;; hawk event has keys :kind (:create :modify :delete)
    ;; and :file #object[java.io.File].
    ;; there is :filter option with the same params as :handler
    (hawk/watch! [{:paths source-paths
                   :handler (fn [ctx ev]
                              (build-once)
                              ctx)
                   }])
    ))

(defn stop-build! [watcher]
  (hawk/stop! watcher))
