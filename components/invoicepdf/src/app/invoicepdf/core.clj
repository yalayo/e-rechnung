(ns app.invoicepdf.core
	(:require [app.invoicerules.interface :refer [invoice-content]]
            [app.product.interface :refer [get-session-products]]
            [app.customer.interface :refer [get-selected-customer]]
						[clj-pdf.core :as pdf]
						[clojure.data.xml :as xml]
						[pdfboxing.merge :as pdfbox])
  (:import [java.io ByteArrayOutputStream]))

(defn generate-xml []
	;; This function should generate XML from the session.
	;; Refer to earlier examples for XML generation.
	"<xml></xml>")  ; Placeholder

(defn generate-pdf [session-id]
  (let [customer-id (get-selected-customer session-id)
        products (filter #(:selected %) (get-session-products session-id))
        output (ByteArrayOutputStream.)]
    (pdf/pdf
     [{:title "Invoice"
       :subject "Betriebskostenabrechnung"
       :author "CCS GmbH"
       :font {:family "Helvetica" :size 12}}
    
             ;; Title Section
      [:heading {:size 16} "Rechnung"]
    
          ;; Invoice Metadata Section
      [:table {:widths [100 200] :border false}
       ["Invoice Number:" "471102"]
       ["Invoice Date:" "05.03.2018"]
       ["Currency:" "EUR"]
       ["Billing Period:" "01.01.2010 to 31.12.2010"]]
    
      [:table {:widths [100 100] :border false}
       [[:cell
         [:heading {:style {:size 15}} "Seller"]
         [:paragraph "CCS GmbH"]
         [:paragraph "Oberer Markt 9, DE 92507 Nabburg"]
         [:paragraph "Tax Number: -"]
         [:paragraph "VAT ID: DE111122223"]]
        [:cell
         [:heading {:style {:size 15}} "Buyer"]
         [:paragraph "Beispielmieter GmbH"]
         [:paragraph "Verwaltung Straße 40, DE 12345 Musterstadt"]]]]
    
          ;; Items and Charges Section
      [:heading {:style {:size 15}} "Invoice Items"]
      [:table {:widths [30 70 180 60 60] :spacing 5}
       ["Stk" "Art. Nummer" "Beschreibung" [:cell {:align :center} "Stückpreis"] [:cell {:align :center} "Betrag"]]
       (first (map #(conj [] [:cell {:align :center} (:quantity %)] (:article-id %) (:description %) [:cell {:align :center} (:unit-price %)] [:cell {:align :center} (Float/toString (* (:unit-price %) (:quantity %)))]) products))]
    
      ] output)
      (.toByteArray output)))

(defn create-invoice [session-id]
	(let [xml-content (generate-xml)]
		(generate-pdf session-id)))