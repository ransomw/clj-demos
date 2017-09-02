(ns setsail.routes.core
  (:require
   [compojure.core :refer
    [ANY GET PUT POST DELETE
     routes defroutes context]]
   [compojure.route :refer [resources]]
   [setsail.routes.helpers :refer
    [load-static-asset make-edn-resp route-print]
    :rename {load-static-asset load-static-asset-global}]
   [setsail.db.core :as db]
   ))

(defn load-static-asset [path]
  (load-static-asset-global (str "assets/demo/" path)))

(defn handle-thing-one [name]
  (do
    (let [new-id
          (db/add-a-thingone! name)]
      (make-edn-resp
       {:new-id new-id})
      )))

(defroutes routes-main
  (GET "/" _
       {:status 200
        :headers {"Content-Type" "text/html; charset=utf-8"}
        :body (load-static-asset-global "public/index.html")})
  (context
   "/demo" []
   (routes
    (GET "/hello" []
         "Hello, demo..")
    (GET "/vid" []
         {:status 200
          :headers {"Content-Type"
                    ;; "video/mp4; charset=utf-8"
                    "video/mp4"
                    }
          :body (load-static-asset
                 "vid/mor-iced-tea.mp4")})
    (GET "/pic" []
         {:status 200
          :headers {"Content-Type"
                    ;; "image/jpeg; charset=utf-8"
                    "image/jpeg"
                    }
          :body (load-static-asset
                 "img/simp_sea_capn_01.jpg")})
    (GET "/thingone" _
         (make-edn-resp (db/all-the-thingone-names)))
    (POST "/thingone" [name] ;;
          (handle-thing-one name))
    ))
  ;; (resources "/")
  )
