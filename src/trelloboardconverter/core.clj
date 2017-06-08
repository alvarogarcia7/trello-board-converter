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

(defn list-by-name [input name]
  (first
    (filter
      (fn [list] (= (get list "id") name)) (get input "lists"))))

(defn group-by-list [input]
  (map
    (fn [[idList cards]]
      {:name (get (list-by-name input idList) "name") :cards (map #(get % "name") cards)})
    (group-by #(get % "idList") (cards input))))

(defn
  pretty-format
  [grouped-input]
  (flatten (doall (map #(-> [(str (get % :name) ":")
                             ""
                             (flatten (doall (map (fn [item] (-> [(str "* " item)])) (get % :cards))))
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
