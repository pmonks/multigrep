[![Build Status](https://travis-ci.com/pmonks/multigrep.svg?branch=master)](https://travis-ci.com/pmonks/multigrep)
[![Open Issues](https://img.shields.io/github/issues/pmonks/multigrep.svg)](https://github.com/pmonks/multigrep/issues)
[![License](https://img.shields.io/github/license/pmonks/multigrep.svg)](https://github.com/pmonks/multigrep/blob/master/LICENSE)
[![Dependencies Status](https://versions.deps.co/pmonks/multigrep/status.svg)](https://versions.deps.co/pmonks/multigrep)

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

The library provides two functions - `grep` (for searching for text within files) and (since v0.3.0) `greplace!` (for searching and replacing text within files).
[The API documentation](https://pmonks.github.io/multigrep/) has full details, and [the unit tests](https://github.com/pmonks/multigrep/blob/master/test/multigrep/core_test.clj) have comprehensive examples.

## Contributors
[Erik Assum](https://github.com/slipset) - [edumucated me in the use of ```partial```.](https://twitter.com/slipset/status/522620387709169664)

## Developer Information

[GitHub project](https://github.com/pmonks/multigrep)

[Bug Tracker](https://github.com/pmonks/multigrep/issues)

## License

Copyright © 2014 Peter Monks (pmonks@gmail.com)

Distributed under the [Eclipse Public License](http://www.eclipse.org/legal/epl-v10.html) either version 1.0 or (at your option) any later version.
