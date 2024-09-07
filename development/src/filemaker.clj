(ns filemaker
  (:require [clj-pdf.core :as pdf])
  (:import [java.sql DriverManager]
           [com.filemaker.jdbc Driver]
           [java.sql DriverManager ResultSet ResultSetMetaData]))

(def fms-hostname "localhost")
(def fm-filename "CCSRechnungen")
(def fm-user "Admin")
(def fm-password "")

;; Register the JDBC client driver
(try
  (let [driver (new com.filemaker.jdbc.Driver)]
    (println "Driver registered"))
  (catch Exception e
    (println e)))

;; Establish a connection to FileMaker
(defn connect-to-filemaker []
  (try
    (let [con (DriverManager/getConnection (str "jdbc:filemaker://" fms-hostname "/" fm-filename)
                                           fm-user fm-password)]
      (println "Connection established")
      con)
    (catch Exception e
      (println e))))

(defn query-inventory [con]
  (let [query "SELECT * FROM CCSAdressen"]
    (try
      (with-open [stmt (.createStatement con)
                  rs (.executeQuery stmt query)]
        ;; Get all the column names
        (let [rsm (.getMetaData rs)
              column-count (.getColumnCount rsm)
              column-names (for [i (range 1 column-count)]
                             (.getColumnName rsm i))]
          
          (while (.next rs)
            (println "AnfrageID:" (.getString rs "AnfrageID"))
            (println "Firmenname:" (.getString rs "Firmenname"))
            (println "Telefon:" (.getString rs "Telefon"))
            (println "Telefon priv:" (.getString rs "Telefon priv"))
            (println "Fax:" (.getString rs "Fax"))
            (println "Telefon Büro:" (.getString rs "Telefon Büro"))
            (println "Straße:" (.getString rs "Straße"))
            (println "Software:" (.getString rs "Software")))))
      (catch Exception e
        (println e)))))

;; Usage
(query-inventory (connect-to-filemaker))

;; (Firmenname Telefon Telefon priv Fax Telefon Büro Straße Ort Land PLZ Eingabedatum Werbegeschenk Priorität Hinweise Kommentare Zweite Straße Zweiter Ort Zweites Land Zweite PLZ Datum Kundenname Typ Kundennr Seriennr email Anrede GlobalSuche Rabattstaffel UST_Proz Vlies verschickt am Zahlungsart Kto BLZ Zahlungsartcheck UID_Nr Maschine Software Status MWSt_Prozent Checkfeld Einnahmekonto Kontoinhaber Geaendert AnfrageID Adresse f. Export LandGebiet UPS VCard Vliesmuster email kopie IBAN BIC Adr_Checkfeld Zweiter Kundenname Zweiter Firmenname CSV_Exportfeld_Wildix ID NamePrefix Extension TypWildix AdressWildix Vat DocumentType Documentld Mobile Tel_ohne_Leerzeichen Büro_ohne_Leerzeichen Karte MWST_Global kdnr_neu kdnr_alt Kundennr_zum_Überprüfen Datensatznr TelNr_mit_Null Kundennummernüberlauf OSS_LK OSS_Prozent Überprüfung Telefon_Plus Telefon_priv_Plus Telefon_Büro_Plus Fax_Plus Shortcut HomeMobile Type Address Kopf_CSV Debitorennr. GlobalDebitor Export Monkey kdnr_B kdnr_C kdnr_D)

;; Get invoice data
(defn query-invoice [con]
  (let [query "SELECT * FROM CCSArtikeldatei"]
    (try
      (with-open [stmt (.createStatement con)
                  rs (.executeQuery stmt query)]
        ;; Get all the column names
        (let [rsm (.getMetaData rs)
              column-count (.getColumnCount rsm)
              column-names (for [i (range 1 column-count)]
                             (.getColumnName rsm i))]
          ;; Iterate through the result set
          (while (.next rs)
            (let [output (apply str (interpose ", "
                                (for [column-name column-names]
                                  (.getString rs column-name))))]
              (println (str output "\n"))))))
      (catch Exception e
        (println e)))))

(comment
  (query-invoice (connect-to-filemaker))
  )

;; Using prepared statement
(defn get-column-value-prepared [con table-name column-name filter-value]
  (let [query (str "SELECT " column-name " FROM " table-name " WHERE " column-name " = ?")]
    (try
      (with-open [stmt (.prepareStatement con query)]
        (.setString stmt 1 filter-value)
        (with-open [rs (.executeQuery stmt)]
          (while (.next rs)
            (println (.getString rs column-name)))))
      (catch Exception e
        (println e)))))

(comment
  (get-column-value-prepared (connect-to-filemaker) "CCSRechnungen" "Artikelnummer" "H803F-XL")
  (get-column-value-prepared (connect-to-filemaker) "CCSArtikeldatei" "ArtNr" "H803F-XL")
  )


;; Without where
(defn get-column [con table-name column-name]
  (let [query (str "SELECT " column-name " FROM " table-name)]
    (try
      (with-open [stmt (.prepareStatement con query)]
        (with-open [rs (.executeQuery stmt)]
          (while (.next rs)
            (println (.getString rs column-name)))))
      (catch Exception e
        (println e)))))

(comment
  (get-column (connect-to-filemaker) "CCSRechnungen" "Kundennummer")
  (get-column (connect-to-filemaker) "CCSArtikeldatei" "ArtBezeichnung")
  )

;; Query a table
(defn get-table [con table-name]
  (let [query (str "SELECT * FROM " table-name)]
    (try
      (with-open [stmt (.prepareStatement con query)]
        (with-open [rs (.executeQuery stmt)
                    rsm (.getMetaData rs)
                    column-count (.getColumnCount rsm)
                    column-names (for [i (range 1 column-count)]
                                   (.getColumnName rsm i))]
          (while (.next rs)
            (let [output (for [column-name column-names]
                           (str column-name ":"(.getString rs column-name)))]
              (println output)))))
      (catch Exception e
        (println e)))))

(comment
  (get-table (connect-to-filemaker) "CCSRechnungen")
  )

;; Query an article
(defn get-article [con article-number]
  (let [query (str "SELECT * FROM CCSArtikeldatei WHERE ArtNr = ?")]
    (try
      (with-open [stmt (.prepareStatement con query)]
        (.setString stmt 1 article-number)
        (with-open [rs (.executeQuery stmt)]
          (let [result (atom {})]
            (while (.next rs)
              (swap! result conj {:quantity 1})
              (swap! result conj {:article-id article-number})
              (swap! result conj {:description (.getString rs "ArtBezeichnung")})
              (swap! result conj {:unit-price (Float/parseFloat (.getString rs "Apreis"))}))
            @result)))
      (catch Exception e
        (println e)))))

(comment
  (get-article (connect-to-filemaker) "H803F-XL")
  )

;; Generate pdf with filemaker data
(defn generate-pdf []
  (pdf/pdf
   [{:title "Invoice"
     :subject "Betriebskostenabrechnung"
     :author "Grundbesitz GmbH & Co."
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
     [[:cell {:align :center} "1"] "H803F-XL" "Abrechnungskreis 1" [:cell {:align :center} "15,387.08"] [:cell {:align :center} "15,387.08"]]]
    
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
   "output/test2.pdf"))

(comment
  (generate-pdf)
  )