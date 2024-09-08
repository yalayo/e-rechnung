(ns app.product.interface
  (:require [app.product.core :as core]))

(defn get-product [article-number]
  (core/get-product article-number))

(defn get-products []
  (core/get-products))