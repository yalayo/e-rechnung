(ns pdf_creation
  (:require [app.invoicerules.interface :refer [invoice-content]]
            [app.invoicepdf.interface :refer [create-invoice]]
            [odoyle.rules :as o]))

(def invoice-data (invoice-content {:invoice-number "123" :invoice-date "2024-08-15"}))

(create-invoice invoice-data "invoice.pdf")