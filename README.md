# multigrep

A little Clojure library that provides regex-based file grepping.

## Installation

multigrep is available as a Maven artifact from [Clojars](https://clojars.org/org.clojars.pmonks/multigrep).
Plonk the following in your project.clj :dependencies, `lein deps` and you should be good to go:

```clojure
[org.clojars.pmonks/multigrep "#.#.#"]
```

The latest version is:

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

The library provides a single multimethod with two arguments:

```
user=> (doc multigrep.core/grep)
-------------------------
multigrep.core/grep
  [r f]
  Returns a sequence of maps representing each of the matches of r (one or more regexes)
  in f (one or more things that can be read by clojure.io/reader).

  Each map in the sequence has these keys:
  {
    :file         ; the file-like thing that matched
    :line         ; text of the line that matched
    :line-number  ; line-number of that line
    :regex        ; the regex that matched this line
    :re-seq       ; the output from re-seq for this line and this regex
  }
nil
```

Regexes are applied on a line-by-line basis using [re-seq](http://clojuredocs.org/clojure_core/clojure.core/re-seq).

Some examples:

```
user=> (require '[clojure.pprint :as pp])
nil
user=> (require '[multigrep.core :as mg])
nil
user=> (slurp "test.txt")
"This is a test.\nThis is a test of the Outdoor Public Warning System.\nThis is only a test."
user=> (mg/grep #"foo" "test.txt")
()
user=> (pp/pprint (mg/grep #"test" "test.txt"))
({:file "test.txt",
  :line "This is a test.",
  :line-number 1,
  :regex #"test",
  :re-seq ("test")}
 {:file "test.txt",
  :line "This is a test of the Outdoor Public Warning System.",
  :line-number 2,
  :regex #"test",
  :re-seq ("test")}
 {:file "test.txt",
  :line "This is only a test.",
  :line-number 3,
  :regex #"test",
  :re-seq ("test")})
nil
user=> (pp/pprint (mg/grep [#"\s([Ss][\w]*)" #"\s((?:(?:P)|(?:O))[\w]*)"] "test.txt"))
({:file "test.txt",
  :line "This is a test of the Outdoor Public Warning System.",
  :line-number 2,
  :regex #"\s([Ss][\w]*)",
  :re-seq ([" System" "System"])}
 {:file "test.txt",
  :line "This is a test of the Outdoor Public Warning System.",
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

[![endorse](https://api.coderwall.com/pmonks/endorsecount.png)](https://coderwall.com/pmonks)

## License

Copyright © 2014 Peter Monks (pmonks@gmail.com)

Distributed under the [Eclipse Public License](http://www.eclipse.org/legal/epl-v10.html) either version 1.0 or (at your option) any later version.
