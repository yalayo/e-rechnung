(ns app.invoicerules.core
	(:require [odoyle.rules :as o]))

(def rules
  (o/ruleset
   {::invoice-header
    [:what
     [::invoice-header ::invoice-number invoice-number]
     [::invoice-header ::invoice-date invoice-date]
     [::invoice-header ::buyer-name buyer-name]
     [::invoice-header ::buyer-address buyer-address]
     [::invoice-header ::seller-name seller-name]
     [::invoice-header ::seller-address seller-address]]}))

(def *session
  (atom (reduce o/add-rule (o/->session) rules)))

(defn update-session [invoice-data]
  (swap! *session
	(fn [session]
		(-> session
				(o/insert ::invoice-header ::invoice-number (:invoice-number invoice-data))
				(o/insert ::invoice-header ::invoice-date (:invoice-date invoice-data))
				(o/insert ::invoice-header ::buyer-name "Buyer")
				(o/insert ::invoice-header ::buyer-address "Munsterstr 23")
				(o/insert ::invoice-header ::seller-name "Seller")
				(o/insert ::invoice-header ::seller-address "Munsterstr 1")
				;; Insert items and totals similarly
				(o/fire-rules)))))

(defn invoice-content [invoice-data]
  (update-session invoice-data) 
  (o/query-all @*session ::invoice-header))