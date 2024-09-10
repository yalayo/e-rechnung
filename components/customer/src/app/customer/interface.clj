(ns app.customer.interface
  (:require [app.customer.core :as core]))

(defn get-customers []
  (core/get-customers))

(defn get-selected-customer [session-id]
  (core/get-selected-customer session-id))

(defn select-customer [session-id customer-id]
  (core/select-customer session-id customer-id))