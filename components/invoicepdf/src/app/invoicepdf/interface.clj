(ns app.invoicepdf.interface
	(:require [app.invoicepdf.core :as core]))

(defn create-invoice [session-id]
	(core/create-invoice session-id))