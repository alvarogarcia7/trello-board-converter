(ns trelloboardconverter.core
  (:gen-class)
  (:require [clojure.data.json :as json]))

(defn
  read-json
  [file]
  (clojure.java.io/reader file))

(def input
  (json/read (read-json "resources/trello2.json")))

(defn -main
  "I don't do trelloboardconverter whole lot ... yet."
  [& args]
  (println "Hello, World!"))
