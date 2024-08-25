(ns app.invoicepdf.core
	(:require [app.invoicerules.interface :refer [invoice-content]]
						[clj-pdf.core :as pdf]
						[clojure.data.xml :as xml]
						[pdfboxing.merge :as pdfbox]))

(defn generate-xml []
	;; This function should generate XML from the session.
	;; Refer to earlier examples for XML generation.
	"<xml></xml>")  ; Placeholder

(defn generate-pdf [pdf-path invoice-content xml-content]
  (pdf/pdf
   [{:title "Invoice"
     :subject "Betriebskostenabrechnung"
     :author "Grundbesitz GmbH & Co."
     :font {:family "Helvetica" :size 12}}
  
       ;; Title Section
    [:heading {:size 16} "INVOICE"]
  
    ;; Invoice Metadata Section
    [:table {:widths [100 200] :border false}
     ["Invoice Number:" "471102"]
     ["Invoice Date:" "05.03.2018"]
     ["Currency:" "EUR"]
     ["Billing Period:" "01.01.2010 to 31.12.2010"]]
  
    [:table {:widths [100 100] :border false}
     [[:cell
       [:heading {:style {:size 15}} "Seller"]
       [:paragraph "Grundbesitz GmbH & Co."]
       [:paragraph "Musterstraße 42, DE 75645 Frankfurt"]
       [:paragraph "Tax Number: 201/113/40209"]
       [:paragraph "VAT ID: DE136695976"]]
      [:cell
       [:heading {:style {:size 15}} "Buyer"]
       [:paragraph "Beispielmieter GmbH"]
       [:paragraph "Verwaltung Straße 40, DE 12345 Musterstadt"]]]]
  
    ;; Items and Charges Section
    [:heading {:style {:size 15}} "Invoice Items"]
    [:table {:widths [30 180 70 60 60 70] :spacing 5}
     [[:cell {:colspan 2} "Description"] "Unit Price (EUR)" "Quantity" "Tax Rate" "Total (EUR)"]
     [[:cell {:align :center} "1"] "Abrechnungskreis 1" [:cell {:align :center} "15,387.08"] [:cell {:align :center} "1"] [:cell {:align :center} "19%"] [:cell {:align :center} "15,387.08"]]]
  
    ;; VAT Breakdown Section
    [:heading {:style {:size 15}} "VAT Breakdown"]
    [:table {:widths [100 100 100 50 50] :spacing 5}
     ["Category" "Value (EUR)" "Base Amount (EUR)" "Tax Rate (%)" "Tax Amount (EUR)"]
     ["VAT S" [:cell {:align :center} "15,387.08"] [:cell {:align :center} "15,387.08"] [:cell {:align :center} "19"] [:cell {:align :center} "2,923.55"]]]
  
    [:pagebreak]
  
     ;; Summary Section
    [:heading {:style {:size 15}} "Summary"]
    [:table {:widths [200 100] :border false}
     ["Subtotal" "15,387.08 EUR"]
     ["Tax Amount" "2,923.55 EUR"]
     ["Total" "18,310.63 EUR"]
     ["Payments Received" "-17,808.00 EUR"]
     ["Balance Due" "502.63 EUR"]]
  
     ;; Payment Terms Section
    [:heading {:style {:size 15}} "Payment Terms"]
    [:paragraph "Due Date: 04.04.2018"]
    [:paragraph "Please ensure payment by the due date to avoid any late fees."]]
   pdf-path)
	;; Attach the XML file to the PDF
	#_(spit "invoice.xml" xml-content)
	#_(pdfbox/merge pdf-path "invoice.xml"))

(defn create-invoice [invoice-data pdf-path]
	(let [xml-content (generate-xml)]
		(generate-pdf pdf-path (invoice-content invoice-data) xml-content)))