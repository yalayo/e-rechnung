(ns app.html.core
	(:require [hiccup2.core :as h]
            [io.pedestal.http.body-params :as body-params]
            [io.pedestal.http.params :as params]
            [ring.util.response :as response]
            [app.html.index :as index]
            [app.html.dashboard :as dashboard]
            [app.html.product :as product]
            [app.html.customer :as customer]))

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

;; Products handlers
(defn products-handler [context]
  (let [session (-> context :session)]
    (if (empty? session)
      (response/redirect "/sign-in")
      (respond-with-params product/content (-> context :session/key) "Products"))))

(def select-product-handler
  {:name ::get
   :enter (fn [context]
            (let [session (-> context :request :session)]
              (if (empty? session)
                (response/redirect "/sign-in")
                (assoc context :response (-> (product/product-selected (-> context :request :session/key) (-> context :request :path-params :product-id)) (ok))))))})

;; Customers handlers
(defn customers-handler [context]
  (let [session (-> context :session)]
    (if (empty? session)
      (response/redirect "/sign-in")
      (respond customer/content "Customers"))))

(def select-customer-handler
  {:name ::get
   :enter (fn [context]
            (let [session (-> context :request :session)]
              (if (empty? session)
                (response/redirect "/sign-in")
                (assoc context :response (-> (customer/customer-selected (-> context :request :session/key) (-> context :request :path-params :customer-id)) (ok))))))})

(def routes
  #{["/" :get index-page-handler :route-name ::index-page]
    ["/dashboard"
     :get [(body-params/body-params) dashboard-handler]
     :route-name ::dashboard]
    ["/products"
     :get [(body-params/body-params) products-handler]
     :route-name ::products]
    ["/products/:product-id"
     :get [params/keyword-params select-product-handler]
     :route-name ::select-product]
    ["/customers"
     :get [customers-handler]
     :route-name ::customers]
    ["/customers/:customer-id"
     :get [params/keyword-params select-customer-handler]
     :route-name ::select-customer]})