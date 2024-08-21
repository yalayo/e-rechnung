(ns app.invoicerules.core
	(:require [odoyle.rules :as o]))

(def rules
	(o/ruleset
		{::add-header-element
		 [:what
			[::invoice-header ::invoice-number value]
			[::invoice-header ::invoice-date value]
			[::invoice-header ::buyer-name value]
			[::invoice-header ::buyer-address value]
			[::invoice-header ::seller-name value]
			[::invoice-header ::seller-address value]]

		 }))

(defn create-session [invoice-data]
	(let [session (o/->session)]
		(-> session
				(o/add-rule rules)
				(o/insert ::invoice-header ::invoice-number "InvoiceNumber")
				(o/insert ::invoice-header ::invoice-date "01.01.2025")
				(o/insert ::invoice-header ::buyer-name "Buyer")
				(o/insert ::invoice-header ::buyer-address "Munsterstr 23")
				(o/insert ::invoice-header ::seller-name "Seller")
				(o/insert ::invoice-header ::seller-address "Munsterstr 1")
				;; Insert items and totals similarly
				(o/fire-rules))))

(defn invoice-content [invoice-data]
	(o/query-all (create-session invoice-data) ::invoice-header))