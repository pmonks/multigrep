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

(ns multigrep.core
  (:require [clojure.java.io :as io]
            [clojure.string  :as s]))

(defn- grep-line
  "Greps a single line with multiple regexes, returning a sequence of matches."
  [file regexes line-number line]
  (remove nil? (map #(let [matches (re-seq % line)]
                       (if (not-empty matches)
                         {
                           :file        file
                           :line        line
                           :line-number line-number
                           :regex       %
                           :re-seq      matches
                         }))
                    regexes)))

(defmulti grep
  "[r f]
  Returns a sequence of maps representing each of the matches of r (one or more regexes) in f (one or more things that can be read by clojure.io/reader).

  Each map in the sequence has these keys:
  {
    :file         ; the file-like thing that matched
    :line         ; text of the line that matched
    :line-number  ; line-number of that line (note: 1 based)
    :regex        ; the regex that matched this line
    :re-seq       ; the output from re-seq for this line and this regex
  }"
  (fn [r f] [(sequential? r) (sequential? f)]))

(defmethod grep [false false]
  [regex file]
  (grep [regex] file))

(defmethod grep [false true]
  [regex files]
  (flatten (map (partial grep [regex]) files)))

(defmethod grep [true false]
  [regexes file]
  (with-open [reader (io/reader file)]
    (let [lines (line-seq reader)]
      (doall (flatten (map-indexed #(grep-line file regexes (inc %1) %2) lines))))))

(defmethod grep [true true]
  [regexes files]
  (flatten (map (partial grep regexes) files)))


(defmulti greplace
  "[r s f]
  Searches for r (a single regex) in f (one or more things that can be read by clojure.io/reader), substituting s (a string).

  Returns a sequence of maps representing each of the substitutions.  Each map in the sequenced has these keys:
  {
    :file         ; the file-like thing that matched
    :line-number  ; line-number of the line that had one or more substitutions (note: 1 based)
  }"
  (fn [r s f] (sequential? f)))

; Threshold below which replacements will be done in-memory - default is 1MB
;(def ^:private in-memory-threshold (* 1024 1024))
(def ^:private in-memory-threshold 1024)   ;####TEST WITH SMALL FILES!!!!

(defn- greplace-and-write-line
  [^java.io.Writer out regex swap line file line-number]
  (let [replaced-line (s/replace line regex swap)]
    (.write out (str replaced-line "\n"))
    (when-not (= line replaced-line)
      {
        :file        file
        :line-number line-number
      })))

(defn- in-memory-greplace-and-write-file
  [regex swap lines file]
  (with-open [out (io/writer file :append false)]
    (doall (remove nil?
                   (flatten (map-indexed #(greplace-and-write-line out regex swap %2 file (inc %1))
                                         lines))))))

(defn- in-memory-greplace
  "Performs a greplace in memory."
  [regex swap file]
  (let [lines (with-open [r (io/reader file)]
                (doall (line-seq r)))]
    (in-memory-greplace-and-write-file regex swap lines file)))

(defn- on-disk-greplace
  "Performs a greplace on disk, a line at a time."
  [regex swap file]
  (let [result    (atom '())
        temp-file (java.io.File/createTempFile "greplace_" ".tmp")]
    (try
      (with-open [temp-w (io/writer temp-file)]
        (io/copy file temp-file))
      (with-open [temp-r (io/reader temp-file)]
        (with-open [out (io/writer file :append false)]
          (reset! result
                  (doall
                    (remove nil?
                            (flatten (map-indexed #(greplace-and-write-line out regex swap %2 file (inc %1))
                                     (line-seq temp-r))))))))
      (finally 
        (io/delete-file temp-file)))
    @result))

(defmethod greplace false
  [regex swap file]
  (if (< (.length (io/file file)) in-memory-threshold)
    (in-memory-greplace regex swap file)
    (on-disk-greplace   regex swap file)))

(defmethod greplace true
  [regex swap files]
  (flatten (map (partial greplace regex swap) files)))
