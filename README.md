[![Build Status](https://travis-ci.org/pmonks/multigrep.svg?branch=master)](https://travis-ci.org/pmonks/multigrep)
[![Open Issues](https://img.shields.io/github/issues/pmonks/multigrep.svg)](https://github.com/pmonks/multigrep/issues)
[![License](https://img.shields.io/github/license/pmonks/multigrep.svg)](https://github.com/pmonks/multigrep/blob/master/LICENSE)
[![Dependencies Status](http://jarkeeper.com/pmonks/multigrep/status.svg)](http://jarkeeper.com/pmonks/multigrep)

# multigrep

A little Clojure library that provides regex-based file grepping and/or text substitution.

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

The library provides two functions - `grep` and (since v0.3.0) `greplace!`:

### grep

The first function, for grepping files, has two arguments:

```
user=> (doc multigrep.core/grep)
-------------------------
multigrep.core/grep
  [r f]
  Returns a sequence of maps representing each of the matches of r (one or more regexes) in f (one or more things that can be read by clojure.io/reader).

  Each map in the sequence has these keys:
  {
    :file         ; the entry in f that matched
    :line         ; text of the line that matched
    :line-number  ; line-number of that line (note: 1 based)
    :regex        ; the entry in r that matched
    :re-seq       ; the output from re-seq for this line and this regex
  }
nil
```

Regexes are applied on a line-by-line basis using [re-seq](http://clojuredocs.org/clojure.core/re-seq).

### greplace!

The second function, for substituting text in files, has 3 or 4 arguments (the last argument is optional):

```
user=> (doc multigrep.core/greplace!)
-------------------------
multigrep.core/greplace!
  [r s f (in-memory-threshold?)]
  Applies r (a single regex) to f (one or more things that can be read by clojure.io/reader), substituting s (a string, or a function of one parameter (the match(es) from the regex) returning a string).

  Returns a sequence of maps representing each of the substitutions.  Each map in the sequence has these keys:
  {
    :file         ; the entry in f that matched
    :line-number  ; line-number of the line that had one or more substitutions (note: 1 based)
  }

  The optional fourth parameter specifies at what file size processing should switch from in-memory to on-disk.  It defaults to 1MB.
nil
```

Substitutions are performed on a line-by-line basis using [clojure.string/replace](http://clojuredocs.org/clojure.string/replace).

## Examples

```
user=> (slurp "test.txt")
"This is a test.\nThis is a test of the Outdoor Public Warning System.\nThis is only a test.\n"
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
user=> (pp/pprint (mg/greplace! #"test" "TEST" "test.txt"))
({:file "test.txt", :line-number 1}
 {:file "test.txt", :line-number 2}
 {:file "test.txt", :line-number 3})
user=> (slurp "test.txt")
"This is a TEST.\nThis is a TEST of the Outdoor Public Warning System.\nThis is only a TEST.\n"
user=> (pp/pprint (mg/greplace! #"(?i)test" (fn [x] (str (java.util.UUID/randomUUID))) "test.txt"))
({:file "test.txt", :line-number 1}
 {:file "test.txt", :line-number 2}
 {:file "test.txt", :line-number 3})
nil
user=> (slurp "test.txt")
"This is a a559edce-d5b4-432d-8092-f587f867134f.\nThis is a e9d536a1-f2f6-4f99-beab-9f949733d094 of the Outdoor Public Warning System.\nThis is only a a914a7b1-baab-4422-a86c-f29370fbf7d5.\n"
```

More comprehensive examples can be seen in the [unit tests](https://github.com/pmonks/multigrep/blob/master/test/multigrep/core_test.clj).

## Contributors
[Erik Assum](https://github.com/slipset) - [edumucated me in the use of ```partial```.](https://twitter.com/slipset/status/522620387709169664)

## Developer Information

[GitHub project](https://github.com/pmonks/multigrep)

[Bug Tracker](https://github.com/pmonks/multigrep/issues)

## License

Copyright © 2014-2016 Peter Monks (pmonks@gmail.com)

Distributed under the [Eclipse Public License](http://www.eclipse.org/legal/epl-v10.html) either version 1.0 or (at your option) any later version.
