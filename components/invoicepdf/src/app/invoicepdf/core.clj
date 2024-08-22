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
  (let [content [:table {:cell-border false
                         :header [{:backdrop-color [100 100 100]} "Row 1" "Row 2" "Row 3"]
                         :spacing 20}
                 ["foo"
                  [:cell
                   [:phrase {:style :italic :size 18 :family :helvetica :color [200 55 221]}
                    "Hello Clojure!"]]
                  "baz"]
                 ["foo1" [:cell {:color [100 10 200]} "bar1"] "baz1"]
                 ["foo2" "bar2" "baz2"]]] 
   (pdf/pdf
     [{:title "Invoice"}
      [:paragraph "Invoice"]
      ]
     pdf-path))
	;; Attach the XML file to the PDF
	#_(spit "invoice.xml" xml-content)
	#_(pdfbox/merge pdf-path "invoice.xml"))

(defn create-invoice [invoice-data pdf-path]
	(let [xml-content (generate-xml)]
		(generate-pdf pdf-path (invoice-content invoice-data) xml-content)))