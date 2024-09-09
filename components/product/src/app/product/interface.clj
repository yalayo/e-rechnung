(ns app.product.interface
  (:require [app.product.core :as core]))

(defn get-product [article-number]
  (core/get-product article-number))

(defn get-products []
  (core/get-products))

(defn get-session-products [session-id]
  (core/get-session-products session-id))

(defn select-product [session-id product-id]
  (core/select-product session-id product-id))