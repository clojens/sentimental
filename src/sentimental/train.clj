(ns sentimental.train
	(:require [clojure.string :as string])
	(:use [opennlp.nlp]
        [opennlp.tools.train]
        [cheshire.core]
        [clojure.pprint]
        [clojure.java.io]))

(def tokenizer (make-tokenizer "src/models/en-token.bin"))
(def senti-model (train-document-categorization "src/models/sentiment.train"))

(defn get-lines
  "Gets all lines."
  [fname]
  (with-open [r (reader fname)]
    (doall (line-seq r))))

(defn temp-corpus
  "Gets the lines from the subjectivity lexicon file."
  []
	(get-lines "resources/en-subjectivity-lexicon.tff"))

(defn create-hashmap
  "Creates a hashmap from input split on the equal sign."
  [l]
	(let [a (map #(string/split % #"=") l)
        b (into {}
                (for [[k v] a]
                  [(keyword k) v]))]
    b))

(defn process
  "Processes the string input to create a hash-map."
  [s]
	(create-hashmap (tokenizer s)))

(defn corpus
  "Defines the actual corpus mapped fromt the temporary one."
  []
  (vec (map process (temp-corpus))))


(defn stemmed-only
  "Only get stems from the collection."
  [col]
	(filter (fn [h] (= (:stemmed1 h) "y"))
			    col))

(defn by-subj
  "Filter by subject such as strongsubj or weaksubj."
  [col subj]
  (filter (fn [h] (= (:type h)
                     subj))
          col))

(defn by-type
  "Filter by type such as positive, negative or neutral."
  [col type]
  (filter (fn [h] (= (:priorpolarity h)
                     type))
          col))

(defn create-train-str
  "Create a training string."
  [h]
  (let [type (:type h)
        pp  (:priorpolarity h)
        word (:word1 h)]
        (str (name type) "-" pp " " word "\n")))

(defn append-to-file
  "Append string to file."
  [s f]
  (with-open [wrtr (writer f :append true)]
    (.write wrtr s)))

(defn append-stemmed-to-file
  "Append stemmed strings to file."
  [subj type]
  (map (fn [h] (append-to-file (create-train-str h)
                               "src/models/en-sentiment.train"))
       (by-type (by-subj (stemmed-only (corpus))
                         subj)
                type)))

(defn append-all-to-file
  "Appends everything to file."
  [subj type]
  (map  (fn [h] (append-to-file (create-train-str h)
                                "src/models/en-sentiment.train"))
        (by-type (by-subj (corpus)
                          subj)
                 type)))

; (append-all-to-file "strongsubj" "positive")
; (append-all-to-file "weaksubj" "positive")
; (append-all-to-file "strongsubj" "negative")
; (append-all-to-file "weaksubj" "negative")
; (append-all-to-file "strongsubj" "neutral")
; (append-all-to-file "weaksubj" "neutral")