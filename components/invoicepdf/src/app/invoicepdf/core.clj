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
		[{:title "Invoice"}
	;; Generate content from session
		 ]
	pdf-path)
	;; Attach the XML file to the PDF
	(spit "invoice.xml" xml-content)
	#_(pdfbox/merge pdf-path "invoice.xml"))

(defn create-invoice [invoice-data pdf-path]
	(let [xml-content (generate-xml)]
		(generate-pdf pdf-path (invoice-content invoice-data) xml-content)))