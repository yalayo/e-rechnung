(ns app.invoicerules.core
	(:require [odoyle.rules :as o]))

(def rules
	(o/ruleset
		{::invoice-header
		 [:what
			[::invoice-number]
			[::invoice-date]
			[::buyer-name]
			[::buyer-address]
			[::seller-name]
			[::seller-address]]

		 ::invoice-item
		 [:what
			[::item-description]
			[::item-quantity]
			[::item-unit-price]
			[::item-total-price]]

		 ::invoice-total
		 [:what
			[::total-amount]]}))

(defn create-session [invoice-data]
	(let [session (o/->session)]
		(-> session
				(o/add-rule rules)
				(o/insert ::invoice-number (invoice-data :invoice-number))
				(o/insert ::invoice-date (invoice-data :invoice-date))
				(o/insert ::buyer-name (invoice-data :buyer-name))
				(o/insert ::buyer-address (invoice-data :buyer-address))
				(o/insert ::seller-name (invoice-data :seller-name))
				(o/insert ::seller-address (invoice-data :seller-address))
				;; Insert items and totals similarly
				(o/fire-rules))))