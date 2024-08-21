(ns app.invoicerules.interface
	(:require [app.invoicerules.core :as core]))

(defn invoice-content [invoice-data]
	(core/invoice-content invoice-data))