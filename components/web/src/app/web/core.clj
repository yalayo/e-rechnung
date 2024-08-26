(ns app.web.core
  (:require [io.pedestal.http :as http]
            [io.pedestal.http.route :as route]
            [io.pedestal.http.body-params :as body-params]
            [io.pedestal.http.params :as params]
            [app.html.interface :refer [index-page]]
            [app.invoicepdf.interface :refer [create-invoice]]
            [app.user.interface :refer [get-routes]]))

(def generate-invoice-handler
  {:name ::post
   :enter (fn [context]
            (let [invoice-data (-> context :request :params)
                  pdf-path "output/invoice.pdf"]
              (create-invoice invoice-data pdf-path)
              (assoc context :response {:status 200
                                        :headers {"Content-Type" "text/html"}
                                        :body "<a href='/download/invoice.pdf'>Download Invoice</a>"})))})

(def routes
  #{["/" :get (fn [_] {:status 200 
                       :headers {"Content-Type" "text/html" "Content-Security-Policy" "img-src 'self'"} 
                       :body (index-page)}) :route-name ::index-page]
    ["/generate-invoice" :post [(body-params/body-params)
                                params/keyword-params
                                generate-invoice-handler] 
     :route-name ::generate-invoice]
    ["/download/:file" :get (fn [{:keys [path-params]}]
                              (println path-params)
                              (let [file-path (str "output/" (:file path-params))]
                                (if (.exists (java.io.File. file-path))
                                  {:status 200
                                   :headers {"Content-Type" "application/pdf"}
                                   :body (java.nio.file.Files/readAllBytes
                                           (java.nio.file.Paths/get file-path (into-array String [])))}
                                  {:status 404 :body "File not found"})))
		 :route-name ::download-file]})

(def service
  {:env :prod
   ::http/routes (route/expand-routes (into #{} (concat routes (get-routes))))
   ::http/resource-path "/public"
   ::http/type :immutant
   ::http/port 8080})

(defn start []
  (http/start (http/create-server service)))

(defn stop [server]
  (http/stop server))