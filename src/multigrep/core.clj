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
  (:require [clojure.java.io :as io]))

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

