(ns app.customer.core
  (:import [java.sql DriverManager]))
  
  (def fms-hostname "localhost")
  (def fm-filename "CCSAdressen")
  (def fm-user "Admin")
  (def fm-password "")
  
  ;; Establish a connection to FileMaker
  (defn connect-to-filemaker []
    (try
      (let [con (DriverManager/getConnection (str "jdbc:filemaker://" fms-hostname "/" fm-filename)
                                             fm-user fm-password)]
        (println "Connected to file - CCAdressen")
        con)
      (catch Exception e
        (println e))))
  
  (defn get-customers []
    (let [query (str "SELECT * FROM CCSAdressen")
          columns ["Kundennr" "Firmenname" "Kundenname" "Straße" "Land" "PLZ" "Ort" "email"]
          customers (atom [])]
      (try
        (with-open [stmt (.prepareStatement (connect-to-filemaker) query)]
          (with-open [rs (.executeQuery stmt)]
            (while (.next rs)
              (let [result (atom {})
                    _ (doseq [column-name columns]
                        (when (= "Kundennr" column-name)
                          (swap! result conj {:customer-id (.getString rs "Kundennr")}))
                        (when (= "Firmenname" column-name)
                          (swap! result conj {:company-name (.getString rs "Firmenname")}))
                        (when (= "Kundenname" column-name)
                          (swap! result conj {:customer-name (.getString rs "Kundenname")}))
                        (when (= "Straße" column-name)
                          (swap! result conj {:street (.getString rs "Straße")}))
                        (when (= "Land" column-name)
                          (swap! result conj {:country (.getString rs "Land")}))
                        (when (= "PLZ" column-name)
                          (swap! result conj {:postal-code (.getString rs "PLZ")}))
                        (when (= "Ort" column-name)
                          (swap! result conj {:city (.getString rs "Ort")}))
                        (when (= "email" column-name)
                          (swap! result conj {:email (.getString rs "email")})))]
                (swap! customers conj @result)))))
        @customers
        (catch Exception e
          (println e)))))

(comment
  (get-customers)
  )