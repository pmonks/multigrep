# multigrep

A little Clojure library that provides various kinds of regex-based file grepping, specifically:

1. single file / single regex grep
2. multi-file / single regex grep
3. single file / multi-regex grep
3. multi-file / multi-regex grep

## Installation

`multigrep` is available as a Maven artifact from [Clojars](https://clojars.org/org.clojars.pmonks/multigrep):

[![version](https://clojars.org/org.clojars.pmonks/multigrep/latest-version.svg)](https://clojars.org/org.clojars.pmonks/multigrep)

## Usage

The multigrep functionality is provided by the `multigrep.core` namespace.

Require it in the REPL:

```clojure
(require '[multigrep.core :as mg])
```

Require it in your application:

```clojure
(ns my-app.core
  (:require [multigrep.core :as mg]))
```

The library provides the following functions:

 * grep-file
 * grep-files
 * multigrep-file
 * multigrep-files

All of which take two parameters - the first being either a single regex (grep-...) or a sequence of regexes (multigrep-...),
and the second being either a single file-like thing (...-file) or a sequence of file-like things (...-files).
"File-like things" means anything that [clojure.java.io/reader](http://clojuredocs.org/clojure_core/clojure.java.io/reader) can
construct a reader from.

Regexes are applied on a line-by-line basis using [re-seq](http://clojuredocs.org/clojure_core/clojure.core/re-seq), and any lines
that contain one or more matches are added to the result.  The result is a flat sequence of maps, each of which has the following keys:

```clojure
{
  :file         <file-like thing>                        ; the file that matched (only provided for the ...-files versions)
  :line         <String>                                 ; text of the line that matched
  :line-number  <Long>                                   ; line-number of that line in the file
  :regex        <regex>                                  ; the regex that matched this line in the file (only provided for the multigrep-... versions)
  :re-seq       <re-seq output (specific to the regex)>  ; the output from re-seq for this file, line and regex
}
```

Some examples:

```clojure
user=> (require '[clojure.pprint :as pp])
nil
user=> (require '[multigrep.core :as mg])
nil
user=> (slurp "test.txt")
"This is a test.\nThis is a test of the Outdoor Public Warning System.\nThis is only a test."
user=> (mg/grep-file #"foo" "test.txt")
()
user=> (pp/pprint (mg/grep-file #"test" "test.txt"))
({:line "This is a test.", :line-number 1, :re-seq ("test")}
 {:line "This is a test of the Outdoor Public Warning System.",
  :line-number 2,
  :re-seq ("test")}
 {:line "This is only a test.", :line-number 3, :re-seq ("test")})
nil
user=> (pp/pprint (mg/multigrep-file [#"\s([Ss][\w]*)" #"\s((?:(?:P)|(?:O))[\w]*)"] "test.txt"))
({:line "This is a test of the Outdoor Public Warning System.",
  :line-number 2,
  :regex #"\s([Ss][\w]*)",
  :re-seq ([" System" "System"])}
 {:line "This is a test of the Outdoor Public Warning System.",
  :line-number 2,
  :regex #"\s((?:(?:P)|(?:O))[\w]*)",
  :re-seq ([" Outdoor" "Outdoor"] [" Public" "Public"])})
nil
```

More comprehensive examples can be seen in the [unit tests](https://github.com/pmonks/multigrep/blob/master/test/multigrep/core_test.clj).

## Developer Information

[GitHub project](https://github.com/pmonks/multigrep)

[Bug Tracker](https://github.com/pmonks/multigrep/issues)

[![Build Status](https://travis-ci.org/pmonks/multigrep.png?branch=master)](https://travis-ci.org/pmonks/multigrep)

## License

Copyright © 2014 Peter Monks (pmonks@gmail.com)

Distributed under the [Eclipse Public License](http://www.eclipse.org/legal/epl-v10.html) either version 1.0 or (at your option) any later version.
