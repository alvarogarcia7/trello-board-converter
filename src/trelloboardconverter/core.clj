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
  pretty-print
  [grouped-input]
  (doall (map #(do (println (str (get % :name) ":"))
                   (println "")
                   (doall (map (fn [item] (println (str "* " item))) (get % :cards)))
                   (println ""))
              grouped-input)))

(defn -main
  "I don't do trelloboardconverter whole lot ... yet."
  [& args]
  (->> args
       first
       read-json
       json/read
       group-by-list
       pretty-print))

;; (filter #(= (get % "idList") "55bf9909d8050ff4a63fc599") (get input "cards"))

;; cards grouped by list
;; (group-by #(get % "idList") cards)

;; get all 1st level attributes
;; (map (fn [[a,b]] a) input)
