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

(defproject org.clojars.pmonks/multigrep "0.2.0-SNAPSHOT"
  :description        "A little library that provides various kinds of regex-based file grepping."
  :url                "https://github.com/pmonks/multigrep"
  :license            {:name "Eclipse Public License"
                       :url  "http://www.eclipse.org/legal/epl-v10.html"}
  :min-lein-version   "2.0.0"
  :dependencies       [[org.clojure/clojure "1.5.1"]]
  :profiles           {:dev     {:dependencies [[midje      "1.6.2"]]
                                 :plugins      [[lein-midje "3.0.1"]]}
                       :uberjar {:aot :all}}
  :uberjar-merge-with {#"META-INF/services/.*" [slurp str spit]}   ; See https://github.com/technomancy/leiningen/issues/1455
  )
