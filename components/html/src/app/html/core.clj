(ns app.html.core
	(:require [hiccup2.core :as h]
            [io.pedestal.http.body-params :as body-params]
            [ring.util.response :as response]
            [app.html.index :as index]
            [app.html.dashboard :as dashboard]
            [app.html.product :as product]))

;; Prepare the hicup to return it as html
(defn template [html-body title]
  [:html
   [:head
    [:title title]
    [:link {:href "tailwind.min.css" :rel "stylesheet"}]
    [:script {:src "htmx.min.js"}]]
   [:body (h/raw html-body)]])

(defn ok [body]
  {:status 200
   :headers {"Content-Type" "text/html" "Content-Security-Policy" "img-src 'self'"}
   :body (-> body
             (h/html)
             (str))})

(defn respond [content title]
  (ok (template (str (h/html (content))) title)))

(defn respond-with-params [content value title]
  (ok (template (str (h/html (content value))) title)))

(defn index-page-handler [context]
  (respond index/index-page "Herzlich Willkommen!"))

(defn dashboard-handler [context]
  (let [session (-> context :session)]
    (if (empty? session)
      (response/redirect "/sign-in")
      (respond-with-params dashboard/content {:email (:email session) :created-at (:created-at session)} "Dashboard"))))

(defn products-handler [context]
  (let [session (-> context :session)]
    (if (empty? session)
      (response/redirect "/sign-in")
      (respond-with-params product/content {:email (:email session) :created-at (:created-at session)} "Dashboard"))))

(def routes
  #{["/" :get index-page-handler :route-name ::index-page]
    ["/dashboard"
     :get [(body-params/body-params) dashboard-handler]
     :route-name ::dashboard]
    ["/products"
     :get [(body-params/body-params) products-handler]
     :route-name ::products]})