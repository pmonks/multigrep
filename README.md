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

## Tested Versions

multigrep is [tested on](https://travis-ci.com/pmonks/multigrep):

|                           | JVM v1.6         | JVM v1.7       | JVM v1.8        | JVM v9         | JVM v10        | JVM v11         |
|                      ---: |  :---:           |  :---:         |  :---:          |  :---:         |  :---:         |  :---:          |
| Clojure 1.4.0             | ❌<sup>1,2</sup> | ❌<sup>1</sup> | ❌<sup>1</sup> | ❌<sup>1</sup> | ❌<sup>1</sup> | ❌<sup>1</sup> |
| Clojure 1.5.1             | ❌<sup>2</sup>   | ✅             | ✅             | ✅             | ✅             | ✅             |
| Clojure 1.6.0             | ❌<sup>2</sup>   | ✅             | ✅             | ✅             | ✅             | ✅             |
| Clojure 1.7.0             | ❌<sup>2</sup>   | ✅             | ✅             | ✅             | ✅             | ✅             |
| Clojure 1.8.0             | ❌<sup>2</sup>   | ✅             | ✅             | ✅             | ✅             | ✅             |
| Clojure 1.9.0             | ❌<sup>2</sup>   | ✅             | ✅             | ✅             | ✅             | ✅             |
| Clojure 1.10.0 (snapshot) | ❌<sup>2,3</sup> | ❌<sup>3</sup> | ✅             | ✅             | ✅             | ✅             |

<sup>1</sup> I chose to only go back as far as Clojure v1.5.1.  If anyone needs this on older versions, PRs are welcome!

<sup>2</sup> Leiningen v2.8 only supports JVM v1.7 and up

<sup>3</sup> Clojure v1.10 only supports JVM v1.8 and up

## Contributors
[Erik Assum](https://github.com/slipset) - [edumucated me in the use of ```partial```.](https://twitter.com/slipset/status/522620387709169664)

## Developer Information

[GitHub project](https://github.com/pmonks/multigrep)

[Bug Tracker](https://github.com/pmonks/multigrep/issues)

## License

Copyright © 2014 Peter Monks (pmonks@gmail.com)

Distributed under the [Eclipse Public License](http://www.eclipse.org/legal/epl-v20.html) either version 2.0 or (at your option) any later version.
