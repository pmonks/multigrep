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
  "Returns a sequence of maps representing each of the matches of r (one or more regexes) in f (one or more things that can be read by clojure.io/reader).

Each map in the sequence has these keys:
  {
    :file         ; the entry in f that matched
    :line         ; text of the line that matched
    :line-number  ; line-number of that line (note: 1 based)
    :regex        ; the entry in r that matched
    :re-seq       ; the output from re-seq for this line and this regex
  }"
  {:arglists '([r f])}
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


(defmulti greplace!
  "Applies r (a single regex) to f (one or more things that can be read by clojure.io/reader), substituting s (a string, or a function of one parameter (the match(es) from the regex) returning a string).

Returns a sequence of maps representing each of the substitutions.  Each map in the sequence has these keys:
  {
    :file         ; the entry in f that matched
    :line-number  ; line-number of the line that had one or more substitutions (note: 1 based)
  }

The optional fourth parameter specifies at what file size processing should switch from in-memory to on-disk.  It defaults to 1MB."
  {:arglists '([r s f]
               [r s f in-memory-threshold])}
  (fn
    ([r s f]           :add-default-threshold)
    ([r s f threshold] (sequential? f))))

(def ^:private default-in-memory-greplace-threshold (* 1024 1024))  ; 1MB

(defn- greplace-and-write-line
  [^java.io.Writer out regex substitution line file line-number]
  (let [replaced-line (s/replace line regex substitution)]
    (.write out (str replaced-line "\n"))
    (when-not (= line replaced-line)
      {
        :file        file
        :line-number line-number
      })))

(defn- greplace-and-write-file
  [regex substitution lines file]
  (with-open [out (io/writer file :append false)]
    (doall (remove nil?
                   (flatten (map-indexed #(greplace-and-write-line out regex substitution %2 file (inc %1))
                                         lines))))))

(defn- in-memory-greplace
  "Performs a greplace in memory."
  [regex substitution file]
  (let [lines (with-open [r (io/reader file)]
                (doall (line-seq r)))]
    (greplace-and-write-file regex substitution lines file)))

(defn- on-disk-greplace
  "Performs a greplace on disk, a line at a time."
  [regex substitution file]
  (let [result    (atom '())
        temp-file (java.io.File/createTempFile "greplace_" ".tmp")]
    (try
      (with-open [temp-w (io/writer temp-file)]
        (io/copy (io/file file) temp-file))
      (with-open [temp-r (io/reader temp-file)]
        (reset! result (greplace-and-write-file regex substitution (line-seq temp-r) file)))
      (finally
        (io/delete-file temp-file)))
    @result))

(defmethod greplace! :add-default-threshold
  [regex substitution file]
  (greplace! regex substitution file default-in-memory-greplace-threshold))

(defmethod greplace! false
  [regex substitution file in-memory-threshold]
  (if (< (.length (io/file file)) in-memory-threshold)
    (in-memory-greplace regex substitution file)
    (on-disk-greplace   regex substitution file)))

(defmethod greplace! true
  [regex substitution files in-memory-threshold]
  (flatten (map #(greplace! regex substitution % in-memory-threshold) files)))
