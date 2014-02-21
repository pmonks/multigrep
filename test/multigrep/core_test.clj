(ns multigrep.core-test
  (:require [clojure.java.io :as    io]
            [midje.sweet     :refer :all]
            [multigrep.core  :refer :all]))

(def aesop1 (io/resource "The Ants and the Grasshopper.txt"))
(def aesop2 (io/resource "The Cat and the Mice.txt"))
(def aesop3 (io/resource "The Goose With the Golden Eggs.txt"))
(def aesop4 (io/resource "The Hare and the Tortoise.txt"))
(def aesop5 (io/resource "The Man and His Two Wives.txt"))

(def aesops [aesop1 aesop2 aesop3 aesop4 aesop5])

(defn- file-name
  [^java.net.URL url]
  (if (nil? url)
    nil
    (.getName (io/file (.getPath (.toURI url))))))

(defn- file-name-line-number
  [grep-result-map]
  [(file-name (:file grep-result-map)) (:line-number grep-result-map)])

(defn- file-name-line-number-regex
  [grep-result-map]
  [(file-name (:file grep-result-map))
   (:line-number grep-result-map)
   (str (:regex grep-result-map))])  ; Convert regexes to strings since they don't appear to respect equality...

; Simple (non-regex) greps
(fact (map :line-number (grep-file #"Cats" aesop1))
      => '())
(fact (map :line-number (grep-file #"Ants" aesop1))
      => '(1 6))
(fact (map file-name-line-number (grep-files #"Ants" aesops))
      => '(["The Ants and the Grasshopper.txt" 1]
           ["The Ants and the Grasshopper.txt" 6]))
(fact (map :line-number (multigrep-file [#"Goose" #"egg"] aesop3))
      => '(1 4 5 8 10 11))
(fact (map file-name-line-number (multigrep-files [#"Mice" #"Man"] aesops))
      => '(["The Cat and the Mice.txt" 1]
           ["The Cat and the Mice.txt" 4]
           ["The Cat and the Mice.txt" 6]
           ["The Cat and the Mice.txt" 10]
           ["The Man and His Two Wives.txt" 1]
           ["The Man and His Two Wives.txt" 5]
           ["The Man and His Two Wives.txt" 7]
           ["The Man and His Two Wives.txt" 13]))

; Complex (regex) greps
(fact (map :line-number (grep-file #"(ing)|(ed)" aesop4))
      => '(4 5 6 7 8 9 10 12 13 14))
(fact (map file-name-line-number (grep-files #"l(ei)|(ie)" aesops))
      => '(["The Ants and the Grasshopper.txt" 8]
           ["The Hare and the Tortoise.txt" 5]
           ["The Hare and the Tortoise.txt" 6]
           ["The Man and His Two Wives.txt" 17]))
(fact (map file-name-line-number-regex (multigrep-files [#"ei" #"ie" #"\s[Gg]rass"] aesops))
      => '(["The Ants and the Grasshopper.txt" 1 "\\s[Gg]rass"]
           ["The Ants and the Grasshopper.txt" 5 "\\s[Gg]rass"]
           ["The Ants and the Grasshopper.txt" 8 "ei"]
           ["The Ants and the Grasshopper.txt" 8 "ie"]
           ["The Cat and the Mice.txt" 6 "ei"]
           ["The Cat and the Mice.txt" 7 "ei"]
           ["The Cat and the Mice.txt" 15 "ei"]
           ["The Hare and the Tortoise.txt" 5 "ie"]
           ["The Hare and the Tortoise.txt" 6 "ie"]
           ["The Man and His Two Wives.txt" 17 "ie"]))
