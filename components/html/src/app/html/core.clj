(ns app.html.core
	(:require [hiccup.page :refer [html5 include-js include-css]]))

	(defn index-page []
		(html5
			[:head
				[:title "Invoice Generator"]
				(include-css "/css/style.css")]
			[:body
				[:h1 "Generate Invoice"]
				[:form {:hx-post "/generate-invoice" :hx-target "this" :hx-swap "outerHTML"}
				 [:div
					[:label "Invoice Number:"]
					[:input {:type "text" :name "invoice-number"}]]
				 [:div
					[:label "Invoice Date:"]
					[:input {:type "date" :name "invoice-date"}]]
				 ;; Add more fields as needed
				 [:input {:type "submit" :value "Generate Invoice"}]]]))