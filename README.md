[![Build Status](https://travis-ci.com/clj-commons/multigrep.svg?branch=master)](https://travis-ci.com/clj-commons/multigrep)
[![Open Issues](https://img.shields.io/github/issues/clj-commons/multigrep.svg)](https://github.com/clj-commons/multigrep/issues)
[![License](https://img.shields.io/github/license/clj-commons/multigrep.svg)](https://github.com/clj-commons/multigrep/blob/master/LICENSE)
[![Dependencies Status](https://versions.deps.co/clj-commons/multigrep/status.svg)](https://versions.deps.co/clj-commons/multigrep)

# multigrep

A little Clojure library that provides regex-based file grepping and/or text substitution.

## Installation

multigrep is available as a Maven artifact from [Clojars](https://clojars.org/clj-commons/multigrep).  The latest version is:

[![version](https://clojars.org/clj-commons/multigrep/latest-version.svg)](https://clojars.org/clj-commons/multigrep)

### Trying it Out
If you prefer to kick the library's tyres without creating a project, you can use the [`lein try` plugin](https://github.com/rkneufeld/lein-try):

```shell
$ lein try clj-commons/multigrep
```

or (as of v0.4.0), if you have installed the [Clojure CLI tools](https://clojure.org/guides/getting_started#_clojure_installer_and_cli_tools):

```shell
$ clj -Sdeps '{:deps {clj-commons/multigrep {:mvn/version "#.#.#"}}}'  # Where #.#.# is replaced with an actual version number >= 0.4.0
```

Either way, you will be dropped in a REPL with the library downloaded and ready for use.

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
[The API documentation](https://clj-commons.github.io/multigrep/) has full details, and [the unit tests](https://github.com/clj-commons/multigrep/blob/master/test/multigrep/core_test.clj) have comprehensive examples.

The [cljdoc](https://cljdoc.org/) project also publishes [up-to-date documentation for this project](https://cljdoc.org/d/clj-commons/multigrep/CURRENT).

## Tested Versions

multigrep is [tested on](https://travis-ci.com/clj-commons/multigrep):

|                | JVM v1.6         | JVM v1.7       | JVM v1.8        | JVM v9         | JVM v10        | JVM v11         |
|           ---: |  :---:           |  :---:         |  :---:          |  :---:         |  :---:         |  :---:          |
| Clojure 1.4.0  | ❌<sup>1,2</sup> | ❌<sup>1</sup> | ❌<sup>1</sup> | ❌<sup>1</sup> | ❌<sup>1</sup> | ❌<sup>1</sup> |
| Clojure 1.5.1  | ❌<sup>2</sup>   | ✅             | ✅             | ✅             | ✅             | ✅             |
| Clojure 1.6.0  | ❌<sup>2</sup>   | ✅             | ✅             | ✅             | ✅             | ✅             |
| Clojure 1.7.0  | ❌<sup>2</sup>   | ✅             | ✅             | ✅             | ✅             | ✅             |
| Clojure 1.8.0  | ❌<sup>2</sup>   | ✅             | ✅             | ✅             | ✅             | ✅             |
| Clojure 1.9.0  | ❌<sup>2</sup>   | ✅             | ✅             | ✅             | ✅             | ✅             |
| Clojure 1.10.0 | ❌<sup>2,3</sup> | ❌<sup>3</sup> | ✅             | ✅             | ✅             | ✅             |

<sup>1</sup> I chose to only go back as far as Clojure v1.5.1.  If anyone needs this on older versions, PRs are welcome!

<sup>2</sup> Leiningen v2.8 only supports JVM v1.7 and up

<sup>3</sup> Clojure v1.10 only supports JVM v1.8 and up

## Contributors
[Erik Assum](https://github.com/slipset) - [edumucated me in the use of ```partial```.](https://twitter.com/slipset/status/522620387709169664)

## Developer Information

[GitHub project](https://github.com/clj-commons/multigrep)

[Bug Tracker](https://github.com/clj-commons/multigrep/issues)

## License

Copyright © 2014 Peter Monks (pmonks@gmail.com)

Distributed under the [Eclipse Public License](http://www.eclipse.org/legal/epl-v20.html) either version 2.0 or (at your option) any later version.
