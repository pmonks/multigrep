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

(defproject org.clojars.pmonks/multigrep "0.3.0"
  :description        "A little Clojure library that provides regex-based file grepping and/or text substitution."
  :url                "https://github.com/pmonks/multigrep"
  :license            {:name "Eclipse Public License"
                       :url  "http://www.eclipse.org/legal/epl-v10.html"}
  :min-lein-version   "2.5.0"
  :dependencies       [
                        [org.clojure/clojure "1.8.0"]
                      ]
  :profiles           {:dev {:dependencies [[midje      "1.8.3"]]
                             :plugins      [[lein-midje "3.2"]]}   ; Don't remove this or travis-ci will assplode!
                       :uberjar {:aot :all}}
  :lein-release       {:deploy-via :clojars}
  )
