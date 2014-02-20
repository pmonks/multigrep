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

(fact "There are 0 mentions of 'Cats' in Aesop1" (grep-file #"Cats" aesop1) => '())
(fact "There are 2 mentions of 'Ants' in Aesop1" (map :line-number (grep-file #"Ants" aesop1)) => '(1 6))

(fact "There are 2 mentions of 'Ants' in all Aesops " (map :line-number (grep-file #"Ants" aesop1)) => '(1 6))
