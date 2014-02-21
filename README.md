# multigrep

A little Clojure library that provides various kinds of regex-based file grepping, specifically:

1. single file / single regex grep
2. multi-file / single regex grep
3. single file / multi-regex grep
3. multi-file / multi-regex grep

## Installation

`multigrep` may soon be available as a Maven artifact from [Clojars](http://clojars.org/multigrep):

```clojure
[org.clojars.pmonks/multigrep "0.1.0"]
```

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
"File-like things" means anything that can be passed to clojure.java.io/reader.

Some examples:

```clojure
(mg/grep-file #"foo" "test.txt")
=> ()

(mg/grep-file #"test" "test.txt")
=> ({:line "This is a test of the emergency broadcasting system.", :line-number 1, :re-seq ("test")})

```

## Developer Information

[GitHub project](https://github.com/pmonks/multigrep)

[Bug Tracker](https://github.com/pmonks/multigrep/issues)

[![Build Status](https://travis-ci.org/pmonks/multigrep.png?branch=master)](https://travis-ci.org/pmonks/multigrep)

## License

Copyright © 2014 Peter Monks (pmonks@gmail.com)

Distributed under the Creative Commons Attribution-ShareAlike 3.0 Unported License.
