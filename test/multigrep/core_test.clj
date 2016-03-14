;
; Copyright © 2014-2016 Peter Monks (pmonks@gmail.com)
;
; All rights reserved. This program and the accompanying materials
; are made available under the terms of the Eclipse Public License v1.0
; which accompanies this distribution, and is available at
; http://www.eclipse.org/legal/epl-v10.html
;
; Contributors:
;    Peter Monks - initial implementation

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
(facts "grep"
  (fact "one non-regex grep, one file, no matches"
    (map :line-number (grep #"Cats" aesop1))
        => '())
  (fact "one non-regex grep, one file, multiple matches"
    (map :line-number (grep #"Ants" aesop1))
        => '(1 6))
  (fact "one non-regex grep, multiple files, multiple matches"
    (map file-name-line-number (grep #"Ants" aesops))
        => '(["The Ants and the Grasshopper.txt" 1]
             ["The Ants and the Grasshopper.txt" 6]))
  (fact "multiple non-regex greps, one file, multiple matches"
    (map :line-number (grep [#"Goose" #"egg"] aesop3))
        => '(1 4 5 8 10 11))
  (fact "multiple non-regex greps, multiple files, multiple matches"
    (map file-name-line-number (grep [#"Mice" #"Man"] aesops))
        => '(["The Cat and the Mice.txt" 1]
             ["The Cat and the Mice.txt" 4]
             ["The Cat and the Mice.txt" 6]
             ["The Cat and the Mice.txt" 10]
             ["The Man and His Two Wives.txt" 1]
             ["The Man and His Two Wives.txt" 5]
             ["The Man and His Two Wives.txt" 7]
             ["The Man and His Two Wives.txt" 13]))

  ; Complex (regex) greps
  (fact "single regex grep, single file, multiple matches"
    (map :line-number (grep #"(ing)|(ed)" aesop4))
        => '(4 5 6 7 8 9 10 12 13 14))
  (fact "single regex grep, multiple files, multiple matches"
    (map file-name-line-number (grep #"l(ei)|(ie)" aesops))
        => '(["The Ants and the Grasshopper.txt" 8]
             ["The Hare and the Tortoise.txt" 5]
             ["The Hare and the Tortoise.txt" 6]
             ["The Man and His Two Wives.txt" 17]))
  (fact "multiple regex greps, mutliple files, multiple matches"
    (map file-name-line-number-regex (grep [#"ei" #"ie" #"\s[Gg]rass"] aesops))
        => '(["The Ants and the Grasshopper.txt" 1 "\\s[Gg]rass"]
             ["The Ants and the Grasshopper.txt" 5 "\\s[Gg]rass"]
             ["The Ants and the Grasshopper.txt" 8 "ei"]
             ["The Ants and the Grasshopper.txt" 8 "ie"]
             ["The Cat and the Mice.txt" 6 "ei"]
             ["The Cat and the Mice.txt" 7 "ei"]
             ["The Cat and the Mice.txt" 15 "ei"]
             ["The Hare and the Tortoise.txt" 5 "ie"]
             ["The Hare and the Tortoise.txt" 6 "ie"]
             ["The Man and His Two Wives.txt" 17 "ie"])))

(facts "greplace"
  (let [test-file (io/file "greplace-test-output.txt")]
    (try
      (io/copy (io/file aesop1) test-file)

      (fact "non-regex grep, one file, no substitutions"
        (map :line-number (greplace #"PANTS" "pants" test-file))
        => '())
      (fact "non-regex grep, one file, one substitution"
        (map :line-number (greplace #"THE ANTS" "The Ants" test-file))
        => '(4))
      (fact "regex grep, one file, one substitution"
        (map :line-number (greplace #"(?i)ants" "ANTS" test-file))
        => '(1 4 6 14))

;      (finally
;        (io/delete-file test-file true))
      )))

