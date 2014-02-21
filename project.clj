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

(defproject org.clojars.pmonks/multigrep "0.1.0"
  :description        "A little library that provides various kinds of regex-based file grepping."
  :url                "https://github.com/pmonks/multigrep"
  :license {:name     "Creative Commons Attribution-ShareAlike 3.0 Unported License."
            :url      "http://creativecommons.org/licenses/by-sa/3.0/"}
  :min-lein-version   "2.0.0"
  :dependencies       [[org.clojure/clojure "1.5.1"]]
  :profiles           {:dev     {:dependencies [[midje      "1.6.2"]]
                                 :plugins      [[lein-midje "3.0.1"]]}
                       :uberjar {:aot :all}}
  :uberjar-merge-with {#"META-INF/services/.*" [slurp str spit]}   ; See https://github.com/technomancy/leiningen/issues/1455
  )
