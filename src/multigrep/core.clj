;
; Copyright © 2014 Peter Monks (pmonks@gmail.com)
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

(defn grep-file
  "Returns a sequence of maps representing the matching lines for the given regex in the given file.
   Each map in the sequence has these keys:
   {
     :line         ; text of the line that matched
     :line-number  ; line-number of that line
     :re-seq       ; the output from re-seq for this line and this regex
   }"
  [regex file]
  (with-open [reader (io/reader file)]
    (let [lines (line-seq reader)]
      (doall
        (remove nil? (map-indexed #(let [matches (re-seq regex %2)]
                                     (if (not-empty matches)
                                       {
                                         :line        %2
                                         :line-number (inc %1)
                                         :re-seq      matches
                                       }))
                                  lines))))))

(defn- grep-file-helper
  [regex file]
  (let [result (grep-file regex file)]
    (map #(assoc % :file file) result)))

(defn grep-files
  "Returns a sequence of maps representing the matching lines for the given regex in the given files.
   Each map in the sequence has these keys:
   {
     :file         ; the file that matched
     :line         ; text of the line that matched
     :line-number  ; line-number of that line in the file
     :re-seq       ; the output from re-seq for this file, line and this regex
   }"
  [regex files]
  (flatten (map #(grep-file-helper regex %) files)))

(defn- multigrep-line
  [file regexes line-number line]
  (remove nil? (map #(let [matches (re-seq % line)]
                       (if (not-empty matches)
                         {
                           :line        line
                           :line-number line-number
                           :regex       %
                           :re-seq      matches
                         }))
                    regexes)))

(defn multigrep-file
  "Returns a sequence of maps representing the matching lines for the given regexes in the given file.
   Each map in the sequence has these keys:
   {
     :line         ; text of the line that matched
     :line-number  ; line-number of that line in the file
     :regex        ; the regex that matched this line
     :re-seq       ; the output from re-seq for this line and this regex
   }"
  [regexes file]
  (with-open [reader (io/reader file)]
    (let [lines   (line-seq reader)]
      (doall (flatten (map-indexed #(multigrep-line file regexes (inc %1) %2) lines))))))

(defn- multigrep-file-helper
  [regexes file]
  (let [result (multigrep-file regexes file)]
    (map #(assoc % :file file) result)))

(defn multigrep-files
  "Returns a sequence of maps representing the matching lines for the given regexes in the given files.
   Each map in the sequence has these keys:
   {
     :file         ; the file that matched
     :line         ; text of the line that matched
     :line-number  ; line-number of that line in the file
     :regex        ; the regex that matched this line in the file
     :re-seq       ; the output from re-seq for this file, line and this regex
   }"
  [regexes files]
  (flatten (map #(multigrep-file-helper regexes %) files)))
