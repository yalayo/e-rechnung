(ns app.invoicepdf.interface
	(:require [app.invoicepdf.core :as core]))

(defn create-invoice [invoice-data pdf-path]
	(core/create-invoice invoice-data pdf-path))