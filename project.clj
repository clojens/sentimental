(defproject sentimental "0.1.2"
  :description "A basic sentiment analyzer, based on clojure-opennlp and using the subjectivity lexicon."
  :url "http://github.com/gnarmis/sentimental"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [clojure-opennlp "0.3.0"]
                 [org.clojars.gnarmis/snowball-stemmer "0.1.1-SNAPSHOT"]
                 [cheshire "5.2.0"]]
  :main sentimental.core)