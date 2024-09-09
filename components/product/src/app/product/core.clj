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

(defn query-products []
  (let [query (str "SELECT * FROM CCSArtikeldatei")
        columns ["Qty" "ArtNr" "ArtBezeichnung" "Apreis"]
        articles (atom [])]
    (try
      (with-open [stmt (.prepareStatement (connect-to-filemaker) query)]
        (with-open [rs (.executeQuery stmt)]
          (while (.next rs)
            (let [result (atom {})
                  _ (doseq [column-name columns]
                      (when (= "Qty" column-name)
                        (swap! result conj {:quantity 1}))
                      (when (= "ArtNr" column-name)
                        (swap! result conj {:article-id (.getString rs "ArtNr")}))
                      (when (= "ArtBezeichnung" column-name)
                        (swap! result conj {:description (.getString rs "ArtBezeichnung")}))
                      (when (= "Apreis" column-name)
                        (swap! result conj {:unit-price (Float/parseFloat (.getString rs "Apreis"))})))]
              (swap! articles conj @result)))))
      @articles
      (catch Exception e
        (println e)))))

;; Store selected products temporarily
;; What happens if the server gets offline sudently (maybe i should store the selections of the database)
(def products (atom {}))

;; Query all products
(defn get-products []
  (let [products (query-products)]
    (map #(assoc % :selected true) products)))

(defn select-product [session-id product-id]
  (swap! products assoc session-id product-id))