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
  "[s r f]
  Searches for s (a single regex) in f (one or more things that can be read by clojure.io/reader), replacing with r (a string).

  Returns a sequence of maps representing each of the replacements.  Each map in the sequenced has these keys:
  {
    :file         ; the file-like thing that matched
    :line-number  ; line-number of that line (note: 1 based)
  }"
  (fn [s r f] (sequential? f)))

(def ^:private in-memory-threshold (* 1024 1024))  ; 1MB

(defn- greplace-and-write-line
  [^java.io.Writer out regex replacement line file line-number]
  (let [replaced-line (s/replace line regex replacement)]
    (.write out (str replaced-line "\n"))
    (when-not (= line replaced-line)
      {
        :file        file
        :line-number line-number
      })))

(defn- in-memory-greplace-and-write-file
  [regex replacement lines file]
  (with-open [out (io/writer file :append false)]
    (doall (remove nil?
                   (flatten (map-indexed #(greplace-and-write-line out regex replacement %2 file (inc %1))
                                         lines))))))

(defn- in-memory-greplace
  [regex replacement file]
  (let [lines (with-open [r (io/reader file)]
                (doall (line-seq r)))]
    (in-memory-greplace-and-write-file regex replacement lines file)))

(defn- on-disk-greplace
  [regex replacement file]
  (throw (UnsupportedOperationException. "Not yet implemented.")))

(defmethod greplace false
  [regex replacement file]
  (if (< (.length (io/file file)) in-memory-threshold)
    (in-memory-greplace regex replacement file)
    (on-disk-greplace   regex replacement file)))

(defmethod greplace true
  [regex replacement files]
  (flatten (map (partial greplace regex replacement) files)))
