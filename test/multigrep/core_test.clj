;
; Copyright © 2014 Peter Monks
;
; All rights reserved. This program and the accompanying materials
; are made available under the terms of the Eclipse Public License v2.0
; which accompanies this distribution, and is available at
; http://www.eclipse.org/legal/epl-v20.html
;
; Contributors:
;    Peter Monks - initial implementation
;
; SPDX-License-Identifier: EPL-2.0
;

(ns multigrep.core-test
  (:require [clojure.java.io :as    io]
            [clojure.string  :as    s]
            [clojure.test    :refer :all]
            [multigrep.core  :refer :all]))

(println "\n☔️ Running tests on Clojure" (clojure-version) "/ JVM" (System/getProperty "java.version") (str "(" (System/getProperty "java.vm.name") " v" (System/getProperty "java.vm.version") ")"))

(def aesop1 (io/resource "The Ants and the Grasshopper.txt"))
(def aesop2 (io/resource "The Cat and the Mice.txt"))
(def aesop3 (io/resource "The Goose With the Golden Eggs.txt"))
(def aesop4 (io/resource "The Hare and the Tortoise.txt"))
(def aesop5 (io/resource "The Man and His Two Wives.txt"))

(def aesops [aesop1 aesop2 aesop3 aesop4 aesop5])

(defmulti ^:private file-name type)

(defmethod ^:private file-name nil
  [file]
  nil)

(defmethod ^:private file-name java.lang.String
  [file]
  file)

(defmethod ^:private file-name java.net.URL
  [file]
  (if (nil? file)
    nil
    (.getName (io/file (.getPath (.toURI file))))))

(defmethod ^:private file-name java.io.File
  [file]
  (if (nil? file)
    nil?
    (.getName file)))

(defn- file-name-line-number
  [grep-result-map]
  [(file-name (:file grep-result-map)) (:line-number grep-result-map)])

(defn- file-name-line-number-regex
  [grep-result-map]
  [(file-name (:file grep-result-map))
   (:line-number grep-result-map)
   (str (:regex grep-result-map))])  ; Convert regexes to strings since they don't appear to respect equality...

(deftest grep-tests
  (testing "grep"
    ; Simple (non-regex) greps
    (testing "one non-regex grep, one file, no matches"                   (is (= (map :line-number (grep #"Cats" aesop1))
                                                                                 '())))
    (testing "one non-regex grep, one file, multiple matches"             (is (= (map :line-number (grep #"Ants" aesop1))
                                                                                 '(1 6))))
    (testing "one non-regex grep, multiple files, multiple matches"       (is (= (map file-name-line-number (grep #"Ants" aesops))
                                                                                 '(["The Ants and the Grasshopper.txt" 1]
                                                                                   ["The Ants and the Grasshopper.txt" 6]))))
    (testing "multiple non-regex greps, one file, multiple matches"       (is (= (map :line-number (grep [#"Goose" #"egg"] aesop3))
                                                                                 '(1 4 5 8 10 11))))
    (testing "multiple non-regex greps, multiple files, multiple matches" (is (= (map file-name-line-number (grep [#"Mice" #"Man"] aesops))
                                                                                 '(["The Cat and the Mice.txt" 1]
                                                                                   ["The Cat and the Mice.txt" 4]
                                                                                   ["The Cat and the Mice.txt" 6]
                                                                                   ["The Cat and the Mice.txt" 10]
                                                                                   ["The Man and His Two Wives.txt" 1]
                                                                                   ["The Man and His Two Wives.txt" 5]
                                                                                   ["The Man and His Two Wives.txt" 7]
                                                                                   ["The Man and His Two Wives.txt" 13]))))
    ; Complex (regex) greps
    (testing "single regex grep, single file, multiple matches"           (is (= (map :line-number (grep #"(ing)|(ed)" aesop4))
                                                                                 '(4 5 6 7 8 9 10 12 13 14))))
    (testing "single regex grep, multiple files, multiple matches"        (is (= (map file-name-line-number (grep #"l(ei)|(ie)" aesops))
                                                                                 '(["The Ants and the Grasshopper.txt" 8]
                                                                                   ["The Hare and the Tortoise.txt" 5]
                                                                                   ["The Hare and the Tortoise.txt" 6]
                                                                                   ["The Man and His Two Wives.txt" 17]))))
    (testing "multiple regex greps, multiple files, multiple matches"     (is (= (map file-name-line-number-regex (grep [#"ei" #"ie" #"\s[Gg]rass"] aesops))
                                                                                 '(["The Ants and the Grasshopper.txt" 1 "\\s[Gg]rass"]
                                                                                   ["The Ants and the Grasshopper.txt" 5 "\\s[Gg]rass"]
                                                                                   ["The Ants and the Grasshopper.txt" 8 "ei"]
                                                                                   ["The Ants and the Grasshopper.txt" 8 "ie"]
                                                                                   ["The Cat and the Mice.txt" 6 "ei"]
                                                                                   ["The Cat and the Mice.txt" 7 "ei"]
                                                                                   ["The Cat and the Mice.txt" 15 "ei"]
                                                                                   ["The Hare and the Tortoise.txt" 5 "ie"]
                                                                                   ["The Hare and the Tortoise.txt" 6 "ie"]
                                                                                   ["The Man and His Two Wives.txt" 17 "ie"]))))))

(deftest greplace!-tests
  (testing "greplace!"
    (testing "in-memory"
      (testing "single file"
        (let [test-file "greplace-test-aesop1.txt"]
          (io/copy (io/file aesop1) (io/file test-file))

          (testing "non-regex grep, string substitute, no substitutions" (is (= (map :line-number (greplace! #"PANTS" "pants" test-file))
                                                                                '())))
          (testing "non-regex grep, string substitute, one substitution" (is (= (map :line-number (greplace! #"THE ANTS" "The Ants" test-file))
                                                                                '(4))))
          (testing "regex grep, string substitute, many substitutions"   (is (= (map :line-number (greplace! #"(?i)ants" "ANTS" test-file))
                                                                                '(1 4 6 14))))
          (testing "regex grep, function substitute, many substitutions" (is (= (map :line-number (greplace! #"(?i)grasshopper" (fn [m] (str (java.util.UUID/randomUUID))) test-file))
                                                                                '(1 5))))))
      (testing "multiple files"
        (let [test-files ["greplace-test-aesop1.txt"
                          "greplace-test-aesop2.txt"
                          "greplace-test-aesop3.txt"
                          "greplace-test-aesop4.txt"
                          "greplace-test-aesop5.txt"]]
          (io/copy (io/file aesop1) (io/file "greplace-test-aesop1.txt"))
          (io/copy (io/file aesop2) (io/file "greplace-test-aesop2.txt"))
          (io/copy (io/file aesop3) (io/file "greplace-test-aesop3.txt"))
          (io/copy (io/file aesop4) (io/file "greplace-test-aesop4.txt"))
          (io/copy (io/file aesop5) (io/file "greplace-test-aesop5.txt"))

          (testing "non-regex grep, string substitute, no substitutions"   (is (= (map file-name-line-number (greplace! #"PANTS" "pants" test-files))
                                                                                  '())))
          (testing "non-regex grep, string substitute, many substitutions" (is (= (map file-name-line-number (greplace! #"and the" "AND THE" test-files))
                                                                                  '(["greplace-test-aesop1.txt" 1]
                                                                                    ["greplace-test-aesop2.txt" 1]
                                                                                    ["greplace-test-aesop4.txt" 1]))))
          (testing "regex grep, string substitute, many substitutions"     (is (= (map file-name-line-number (greplace! #"(?i)(and|with) the" "and the" test-files))
                                                                                  '(["greplace-test-aesop1.txt" 1]
                                                                                    ["greplace-test-aesop2.txt" 1]
                                                                                    ["greplace-test-aesop3.txt" 1]
                                                                                    ["greplace-test-aesop4.txt" 1]))))
          (testing "regex grep, function substitute, many substitutions"   (is (= (map file-name-line-number (greplace! #"(?i)(and|with) the" (fn [m] (str (java.util.UUID/randomUUID))) test-files))
                                                                                  '(["greplace-test-aesop1.txt" 1]
                                                                                    ["greplace-test-aesop2.txt" 1]
                                                                                    ["greplace-test-aesop3.txt" 1]
                                                                                    ["greplace-test-aesop4.txt" 1])))))))
    (testing "on-disk"
      (let [in-memory-threshold 0]   ; Force on-disk greplace
        (testing "single file"
          (let [test-file "greplace-test-aesop1.txt"]
            (io/copy (io/file aesop1) (io/file test-file))

            (testing "non-regex grep, string substitute, no substitutions" (is (= (map :line-number (greplace! #"PANTS" "pants" test-file in-memory-threshold))
                                                                                  '())))
            (testing "non-regex grep, string substitute, one substitution" (is (= (map :line-number (greplace! #"THE ANTS" "The Ants" test-file in-memory-threshold))
                                                                                  '(4))))
            (testing "regex grep, string substitute, many substitutions"   (is (= (map :line-number (greplace! #"(?i)ants" "ANTS" test-file in-memory-threshold))
                                                                                  '(1 4 6 14))))
            (testing "regex grep, function substitute, many substitutions" (is (= (map :line-number (greplace! #"(?i)grasshopper" (fn [m] (str (java.util.UUID/randomUUID))) test-file in-memory-threshold))
                                                                                  '(1 5))))))
        (testing "multiple files"
          (let [test-files ["greplace-test-aesop1.txt"
                            "greplace-test-aesop2.txt"
                            "greplace-test-aesop3.txt"
                            "greplace-test-aesop4.txt"
                            "greplace-test-aesop5.txt"]]
            (io/copy (io/file aesop1) (io/file "greplace-test-aesop1.txt"))
            (io/copy (io/file aesop2) (io/file "greplace-test-aesop2.txt"))
            (io/copy (io/file aesop3) (io/file "greplace-test-aesop3.txt"))
            (io/copy (io/file aesop4) (io/file "greplace-test-aesop4.txt"))
            (io/copy (io/file aesop5) (io/file "greplace-test-aesop5.txt"))

            (testing "non-regex grep, string substitute, no substitutions"   (is (= (map file-name-line-number (greplace! #"PANTS" "pants" test-files))
                                                                                    '())))
            (testing "non-regex grep, string substitute, many substitutions" (is (= (map file-name-line-number (greplace! #"and the" "AND THE" test-files))
                                                                                    '(["greplace-test-aesop1.txt" 1]
                                                                                      ["greplace-test-aesop2.txt" 1]
                                                                                      ["greplace-test-aesop4.txt" 1]))))
            (testing "regex grep, string substitute, many substitutions"     (is (= (map file-name-line-number (greplace! #"(?i)(and|with) the" "and the" test-files))
                                                                                    '(["greplace-test-aesop1.txt" 1]
                                                                                      ["greplace-test-aesop2.txt" 1]
                                                                                      ["greplace-test-aesop3.txt" 1]
                                                                                      ["greplace-test-aesop4.txt" 1]))))
            (testing "regex grep, function substitute, many substitutions"   (is (= (map file-name-line-number (greplace! #"(?i)(and|with) the" (fn [m] (str (java.util.UUID/randomUUID))) test-files))
                                                                                    '(["greplace-test-aesop1.txt" 1]
                                                                                      ["greplace-test-aesop2.txt" 1]
                                                                                      ["greplace-test-aesop3.txt" 1]
                                                                                      ["greplace-test-aesop4.txt" 1]))))))))))
