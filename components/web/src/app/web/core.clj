(ns app.web.core
  (:require [io.pedestal.http :as http]
            [html.core :refer [index-page]]
            [invoice-pdf.core :refer [create-invoice]]))

(defn generate-invoice-handler [request]
  (let [invoice-data (-> request :form-params)
        pdf-path "output/invoice.pdf"]
    (create-invoice invoice-data pdf-path)
;; Return response with a link to download the PDF
    {:status 200
     :headers {"Content-Type" "text/html"}
     :body "<a href='/download/invoice.pdf'>Download Invoice</a>"}))

(def routes
  #{["/" :get (fn [_] {:status 200 :headers {"Content-Type" "text/html"} :body (index-page)})]
    ["/generate-invoice" :post generate-invoice-handler]
    ["/download/:file" :get (fn [{:keys [path-params]}]
                              (let [file-path (str "output/" (:file path-params))]
                                (if (.exists (java.io.File. file-path))
                                  {:status 200
                                   :headers {"Content-Type" "application/pdf"}
                                   :body (java.nio.file.Files/readAllBytes (java.nio.file.Paths/get file-path))}
                                  {:status 404 :body "File not found"})))]})

(def service
  {:env :prod
   ::http/routes routes
   ::http/resource-path "/public"
   ::http/type :jetty
   ::http/port 8080})

(defn start []
  (http/start (http/create-server service)))

(defn stop [server]
  (http/stop server))