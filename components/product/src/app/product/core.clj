(ns app.product.core
  (:import [java.sql DriverManager]))

(def fms-hostname "localhost")
(def fm-filename "CCSRechnungen")
(def fm-user "Admin")
(def fm-password "")

;; Establish a connection to FileMaker
(defn connect-to-filemaker []
  (try
    (let [con (DriverManager/getConnection (str "jdbc:filemaker://" fms-hostname "/" fm-filename)
                                           fm-user fm-password)]
      (println "Connection established")
      con)
    (catch Exception e
      (println e))))

;; Query a product
(defn get-product [article-number]
  (let [query (str "SELECT * FROM CCSArtikeldatei WHERE ArtNr = ?")]
    (try
      (with-open [stmt (.prepareStatement (connect-to-filemaker) query)]
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

;; Query a product
(defn get-products []
  (let [query (str "SELECT * FROM CCSArtikeldatei WHERE ArtNr = ?")]
    (try
      (with-open [stmt (.prepareStatement (connect-to-filemaker) query)]
        #_(.setString stmt 1 article-number)
        (with-open [rs (.executeQuery stmt)]
          (let [result (atom {})]
            (while (.next rs)
              (swap! result conj {:quantity 1})
              #_(swap! result conj {:article-id article-number})
              (swap! result conj {:description (.getString rs "ArtBezeichnung")})
              (swap! result conj {:unit-price (Float/parseFloat (.getString rs "Apreis"))}))
            @result)))
      (catch Exception e
        (println e)))))