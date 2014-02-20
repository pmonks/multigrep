;
; Copyright © 2014 Peter Monks (pmonks@gmail.com)
;
; This work is licensed under the Creative Commons Attribution-ShareAlike 3.0
; Unported License. To view a copy of this license, visit
; http://creativecommons.org/licenses/by-sa/3.0/ or send a letter to Creative
; Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.
;

(defproject org.clojars.pmonks/multigrep "0.1.0-SNAPSHOT"
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
