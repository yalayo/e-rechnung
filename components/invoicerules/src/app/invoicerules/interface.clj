(ns app.invoicerules.interface
	(:require [app.invoicerules.core :as core]))

(defn create-session [invoice-data]
	(core/create-session invoice-data))