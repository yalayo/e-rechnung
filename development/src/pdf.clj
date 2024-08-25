(ns pdf
  (:require [clj-pdf.core :as pdf])
  (:import [com.lowagie.text.pdf PdfPCellEvent PdfPTable]))

(pdf/pdf [{}
          [:list {:roman true}
           [:chunk {:style :bold} "a bold item"]
           "another item"
           "yet another item"]
          [:phrase "some text"]
          [:phrase "some more text"]
          [:paragraph "yet more text"]]
         "pdf/doc1.pdf")

(pdf/pdf
 [{}
  (for [i (range 3)]
    [:paragraph (str "item: " i)])]
 "pdf/doc2.pdf")

(defn pdf-table [column-widths & rows]
  (into
   [:pdf-table column-widths]
   (map (partial map (fn [element] [:pdf-cell element])) rows)))

(pdf/pdf
 [{}
  (pdf-table
   [10 20 15]
   ["foo" [:chunk {:style :bold} "bar"] [:phrase "baz"]]
   ["foo" "bar" "baz"]
   ["baz" "foo" "bar"])]
 "pdf/doc3.pdf")

(def stylesheet
  {:foo {:color [255 0 0]
         :family :helvetica}
   :bar {:color [0 0 255]
         :family :helvetica}
   :baz {:align :right}})

(pdf/pdf
 [{:stylesheet stylesheet}
  [:paragraph.foo "item: 0"]
  [:paragraph.bar "item: 1"]
  [:paragraph.bar.baz "item: 2"]]
 "pdf/doc4.pdf")

(pdf/pdf
 [{:stylesheet stylesheet
   :header {
            :table
            [:pdf-table
             {:border false}
             [20 15 60]
             ["this is a table header" "second column" "third column"]]}
   :footer {:table
            [:pdf-table
             {:border false}
             [20 15 60]
             ["this is a table footer" "second column" "third column"]]}}
  [:paragraph.foo "item: 0"]
  [:paragraph.bar "item: 1"]
  [:paragraph.bar.baz "item: 2"]]
 "pdf/doc5.pdf")

(pdf/pdf
 [{:stylesheet stylesheet}
  [:paragraph.foo "item: 0"]
  [:paragraph.bar "item: 1"]
  [:paragraph.bar.baz "item: 2"]
  [:svg {} (clojure.java.io/file "pdf/logo.svg")]]
 "pdf/doc6.pdf")

(pdf/pdf
 [{:stylesheet stylesheet}
  [:paragraph.foo "item: 0"]
  [:paragraph.bar "item: 1"]
  [:paragraph.bar.baz "item: 2"]
  [:svg {} (clojure.java.io/file "pdf/logo.svg")]
  [:table {:header ["Row 1" "Row 2" "Row 3"] :width 50 :border true :cell-border false}
   [[:cell {:colspan 2} "Foo"] "Bar"]
   [[:cell "foo1" " " "foo2"] "bar1" "baz1"]
   ["foo2" "bar2" "baz2"]]]
 "pdf/doc7.pdf")

(pdf/pdf
 [{:stylesheet stylesheet}
  [:paragraph.foo "item: 0"]
  [:paragraph.bar "item: 1"]
  [:paragraph.bar.baz "item: 2"]
  [:svg {} (clojure.java.io/file "pdf/logo.svg")]
  (into
   [:table {:header ["foo" "bar" "baz"]}]
   (for [x (range 1 10)]
     [[:cell {:color [(* 10 x) 0 0]} (dec x)]
      [:cell {:color [0 (* 10 x) 0]} x]
      [:cell {:color [0 0 (* 10 x)]} (inc x)]]))]
 "pdf/doc8.pdf")

(pdf/pdf
 [{}
  [:pdf-table
   {:bounding-box [50 100]
    :horizontal-align :right
    :spacing-before 100
    }
   [10 20 15]
   ["foo" [:chunk {:style :bold} "bar"] [:phrase "baz"]]
   [[:pdf-cell "foo"] [:pdf-cell "foo"] [:pdf-cell "foo"]]
   [[:pdf-cell "foo"] [:pdf-cell "foo"] [:pdf-cell "foo"]]]]
 "pdf/doc9.pdf")

(pdf/pdf
 [{}
  [:pdf-table
   {:width-percent 100}
   nil
   ["foo" [:chunk {:style :bold} "bar"] [:phrase "baz"]]
   [[:pdf-cell "foo"] [:pdf-cell "foo"] [:pdf-cell "foo"]]
   [[:pdf-cell "foo"] [:pdf-cell "foo"] [:pdf-cell "foo"]]]]
 "pdf/doc10.pdf")

(pdf/pdf
 [{}
  [:pdf-table
   {:header [[[:pdf-cell {:colspan 2}
               [:paragraph {:align :center :style :bold} "Customer Orders"]]]
             [[:phrase {:style :bold} "Name"]
              [:phrase {:style :bold} "Order Amount"]]]}
   [50 50]
   ["Joe" "$20.00"]
   ["Bob" "$7.50"]
   ["Mary" "$18.90"]]]
 "pdf/doc11.pdf")

(def border
  (reify
    PdfPCellEvent
    (cellLayout [this cell rect canvas]
      (let [radius 4
            lineWidth 1.5
            cb (get canvas PdfPTable/BACKGROUNDCANVAS)
            [l b w h] [(.getLeft rect) (.getBottom rect) (.getWidth rect) (.getHeight rect)]]
        (.roundRectangle cb l b w h radius)
        (.setLineWidth cb lineWidth)
        (.stroke cb)))))


(pdf/pdf
 [{}
  [:pdf-table {:padding 0}
   [100]
   [[:pdf-cell {:set-border [] :event-handler border}
     [:pdf-table {:event-handler nil :width-percent 100}
      [50 50]
      [[:pdf-cell {:set-border [:bottom :right]} "a"]
       [:pdf-cell {:set-border [:bottom]} "b"]]
      [[:pdf-cell {:set-border [:right]} "d"]
       [:pdf-cell {} "c"]]]]]]]
 "pdf/doc12.pdf")

(pdf/pdf
 [{}
  [:pdf-table
   {:header [[[:pdf-cell {:colspan 2}
               [:paragraph {:align :center :style :bold} "Customer Orders"]]]
             [[:phrase {:style :bold} "Name"]
              [:phrase {:style :bold} "Order Amount"]]]}
   [50 50]
   ["Joe" "$20.00"]
   ["Bob" "$7.50"]
   ["Mary" "$18.90"]]
  [:chart
   {:type "bar-chart"
    :title "Bar Chart"
    :background [10 100 40]
    :x-label "Items"
    :y-label "Quality"}
   [2 "Foo"] [4 "Bar"] [10 "Baz"]]]
 "pdf/doc13.pdf")

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
  
     ;; Seller and Buyer Information Section
  [:heading {:style {:size 15}} "Seller"]
  [:paragraph "Grundbesitz GmbH & Co."]
  [:paragraph "Musterstraße 42, DE 75645 Frankfurt"]
  [:paragraph "Tax Number: 201/113/40209"]
  [:paragraph "VAT ID: DE136695976"]
  
  [:heading {:style {:size 15}} "Buyer"]
  [:paragraph "Beispielmieter GmbH"]
  [:paragraph "Verwaltung Straße 40, DE 12345 Musterstadt"]
  
  [:pagebreak]

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
 "pdf/invoice.pdf")