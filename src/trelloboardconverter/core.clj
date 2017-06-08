(ns trelloboardconverter.core
  (:gen-class)
  (:require [clojure.data.json :as json]))

(defn
  read-json
  [file]
  (clojure.java.io/reader file))

(defn
  cards
  [input]
  (get input "cards"))

(defn list-by-id [input name]
  (let [lists (get input "lists")]
    (first
      (filter
        (fn [list] (= (get list "id") name)) lists))))

(defn group-by-list [input]
  (let [name (fn [item] (get item "name"))]
    (map
      (fn [[idList cards]]
        {:name  (name (list-by-id input idList))
         :cards (map #(name %) cards)})
      (group-by #(get % "idList") (cards input)))))

(defn
  pretty-format
  [grouped-input]
  (let [cards (fn [group] (get group :cards))
        name (fn [group] (get group :name))
        column (fn [name] (str name ":"))
        items-of (fn [cards] (map (fn [item] (-> [(str "  * " item)])) cards))]
    (flatten (map #(->
                     [(column (name %))
                      ""
                      (items-of (cards %))
                      ""])
                  grouped-input))))

(defn -main
  [& args]
  (->> args
       first
       read-json
       json/read
       group-by-list
       pretty-format
       (map println)
       doall))

;; get all 1st level attributes
;; (map (fn [[a,b]] a) input)
