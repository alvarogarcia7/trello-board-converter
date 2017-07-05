(ns trelloboardconverter.core
  (:gen-class)
  (:require [clojure.data.json :as json]
            [clojure.string :as str]))

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
  (let [name (fn [item] (get item "name"))
        addName (fn [item acc] (str acc (name item)))
        labels (fn [item]
                 (let [item-labels (get item "labels")
                       label-names (map #(get % "name") item-labels)]
                   (if (empty? label-names)
                     nil
                     (str/join ", " label-names))))
        addLabels (fn [card acc]
                    (if (nil? (labels card))
                      acc
                      (str acc "; " (labels card))))]
    (map
      (fn [[idList cards]]
        {:name  (name (list-by-id input idList))
         :cards (map #(->> "" (addName %) (addLabels %)) cards)})
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
