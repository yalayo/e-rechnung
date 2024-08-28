(ns app.web.core
  (:require [io.pedestal.http :as http]
            [io.pedestal.http.route :as route]
            [io.pedestal.http.body-params :as body-params]
            [io.pedestal.http.params :as params]
            [io.pedestal.http.ring-middlewares :as middlewares]
            [jdbc-ring-session.core :as jdbc-ring-session]
            [app.html.interface :as html]
            [app.invoicepdf.interface :refer [create-invoice]]
            [app.user.interface :refer [get-routes get-datasource]]))

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
  #{["/generate-invoice" :post [(body-params/body-params)
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

(def session-interceptor
  (middlewares/session {:store (jdbc-ring-session/jdbc-store (get-datasource) {:table :session_store})}))

(def service
  (-> {:env :prod
       ::http/routes (route/expand-routes (into #{} (concat routes (get-routes) (html/get-routes))))
       ::http/resource-path "/public"
       ::http/type :immutant
       ::http/port 8080}
      (http/default-interceptors)
      (update ::http/interceptors concat [session-interceptor])
      http/create-server))

(defn start []
  (http/start service))

(defn stop [server]
  (http/stop server))