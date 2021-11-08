;
; Copyright © 2021 Peter Monks
;
; All rights reserved. This program and the accompanying materials
; are made available under the terms of the Eclipse Public License v2.0
; which accompanies this distribution, and is available at
; http://www.eclipse.org/legal/epl-v20.html
;
; Contributors:
;    Peter Monks - initial implementation
;
; SPDX-License-Identifier: EPL-2.0
;

(ns build
  "Build script for multigrep.

For more information, run:

clojure -A:deps -T:build help/doc"
  (:require [clojure.tools.build.api :as b]
            [org.corfield.build      :as bb]
            [tools-convenience.api   :as tc]
            [tools-pom.tasks         :as pom]
            [tools-licenses.tasks    :as lic]
            [pbr.tasks               :as pbr]))

(def lib       'clj-commons/multigrep)
(def version   (format "1.0.%s" (b/git-count-revs nil)))

; Utility fns
(defn- set-opts
  [opts]
  (assoc opts
         :lib          lib
         :version      version
         :write-pom    true
         :validate-pom true
         :pom          {:description      "A little Clojure library that provides regex-based file grepping and/or text substitution."
                        :url              "https://github.com/clj-commons/multigrep"
                        :licenses         [:license   {:name "Eclipse Public License 2.0" :url "https://www.eclipse.org/legal/epl-2.0/"}]
                        :developers       [:developer {:id "pmonks" :name "Peter Monks" :email "pmonks+multigrep@gmail.com"}]
                        :scm              {:url "https://github.com/clj-commons/multigrep" :connection "scm:git:git://github.com/clj-commons/multigrep.git" :developer-connection "scm:git:ssh://git@github.com/clj-commons/multigrep.git"}
                        :issue-management {:system "github" :url "https://github.com/clj-commons/multigrep/issues"}}))

; Build tasks
(defn clean
  "Clean up the project."
  [opts]
  (bb/clean (set-opts opts)))

(defn check
  "Check the code by compiling it."
  [opts]
  (bb/run-task (set-opts opts) [:check]))

(defn run-tests
  "Run the tests."
  [opts]
  (bb/run-tests opts))

(defn outdated
  "Check for outdated dependencies."
  [opts]
  (bb/run-task (set-opts opts) [:outdated]))

(defn kondo
  "Run the clj-kondo linter."
  [opts]
  (bb/run-task (set-opts opts) [:kondo]))

(defn eastwood
  "Run the eastwood linter."
  [opts]
  (bb/run-task (set-opts opts) [:eastwood]))

(defn lint
  "Run all linters."
  [opts]
  (-> opts
      (kondo)
      (eastwood)))

(defn ci
  "Run the CI pipeline."
  [opts]
  (-> opts
      (outdated)
      (check)
      (lint)
      (run-tests)))

(defn licenses
  "Attempts to list all licenses for the transitive set of dependencies of the project, using SPDX license expressions."
  [opts]
  (-> opts
      (set-opts)
      (lic/licenses)))

(defn check-release
  "Check that a release can be done from the current directory."
  [opts]
  (-> opts
      (set-opts)
      (ci)
      (pbr/check-release)))

(defn release
  "Release a new version of the library."
  [opts]
  (check-release opts)
  (-> opts
      (set-opts)
      (pbr/release)))

(defn jar
  "Generates a PBR library JAR for the project."
  [opts]
  (-> opts
      (set-opts)
      (pom/pom)
      (bb/jar)))

(defn deploy
  "Deploys the PBR library JAR to Clojars."
  [opts]
  (-> opts
      (set-opts)
      (pbr/deploy)))

(defn docs
  "Generates codox documentation"
  [_]
  (tc/ensure-command "clojure")
  (tc/exec "clojure -Srepro -X:codox"))
