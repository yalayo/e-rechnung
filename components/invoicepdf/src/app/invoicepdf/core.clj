(ns app.invoicepdf.core
	(:require [app.invoicerules.interface :refer [create-session]]
						[clj-pdf.core :as pdf]
						[clojure.data.xml :as xml]
						[pdfboxing.merge :as pdfbox]))

(defn generate-xml [session]
	;; This function should generate XML from the session.
	;; Refer to earlier examples for XML generation.
	"<xml></xml>")  ; Placeholder

(defn generate-pdf [pdf-path session xml-content]
	(let [invoice-content (o/query-all session ::invoice-header)
	;; More content generation
				]
		(pdf/pdf
			[{:title "Invoice"}
;; Generate content from session
			 ]
pdf-path))

;; Attach the XML file to the PDF
(spit "invoice.xml" xml-content)
(pdfbox/merge pdf-path "invoice.xml"))

(defn create-invoice [invoice-data pdf-path]
	(let [session (create-session invoice-data)
				xml-content (generate-xml session)]
		(generate-pdf pdf-path session xml-content)))