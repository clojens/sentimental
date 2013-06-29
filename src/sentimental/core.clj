(ns sentimental.core
	(:import [snowball-stemmer])
	(:require [sentimental.train :as tr])
	(:use [stemmer.snowball]
		  	[opennlp.nlp]
		  	[clojure.java.io]))

(def eng-stemmer (stemmer "english"))
(def tokenizer (make-tokenizer "src/models/en-token.bin"))
(def detokenizer (make-detokenizer "src/models/english-detokenizer.xml"))
; the actual categorizer
(def categorize (make-document-categorizer tr/senti-model))


(defn stop-words
  "Loads the file with English stopwords by default, optionally takes a single
  argument string from which it will try to load."
  ([] (set (sentimental.train/get-lines "resources/en_stop_words.txt")))
  ([file] (set (sentimental.train/get-lines file))))


(defn strip-stop-words
  [l]
	(filter (fn [x] (not (contains? (stop-words) x)))
			(set l)))

(defn bag-of-words
  "Converts a string into a set of unique words/elements,
  each stemmed (in English)"
	[s]
	(set (map (fn [x] (eng-stemmer x))
			(strip-stop-words (tokenizer s)))))

(defn compact
	"Takes a string, strips out stop words, and stems each word. Returns a string"
	[s]
	(detokenizer (bag-of-words s)))