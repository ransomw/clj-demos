(ns feels.views.login
  (:require
   [rum.core :as rum]
   ))

(rum/defc login-form < rum/static [token]
  [:form {:action "/login" :method "POST"}
   [:input {:type "text" :name "username" :placeholder "username"}]
   [:input {:type "password" :name "password" :placeholder "password"}]
   [:input {:type "hidden" :name "__anti-forgery-token"
            :value token
            }]
   [:input {:type "submit"} "login"]
   ])

(rum/defc login-page < rum/static [token]
  [:html
   [:head
    [:meta {:charset "UTF-8"}]
    [:meta {:name "viewport"
            :content "width=device-width, initial-scale=1"}]
    ]
   [:body
    (login-form token)
    ]
   ])

(defn make-login-page-str [token]
  (str "<!DOCTYPE html>"
       (rum/render-static-markup
        (login-page token)
        )
       ))
