(ns app.customer.interface
  (:require [app.customer.core :as core]))

(defn get-customers []
  (core/get-customers))

(defn select-customer [session-id customer-id]
  (core/select-customer session-id customer-id))