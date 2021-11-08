| | | |
|---:|:---:|:---:|
| [**main**](https://github.com/clj-commons/multigrep/tree/main) | [![CI](https://github.com/clj-commons/multigrep/workflows/CI/badge.svg?branch=main)](https://github.com/clj-commons/multigrep/actions?query=workflow%3Alint) | [![Dependencies](https://github.com/clj-commons/multigrep/workflows/dependencies/badge.svg?branch=main)](https://github.com/clj-commons/multigrep/actions?query=workflow%3Adependencies) |
| [**dev**](https://github.com/clj-commons/multigrep/tree/dev)  | [![CI](https://github.com/clj-commons/multigrep/workflows/CI/badge.svg?branch=dev)](https://github.com/clj-commons/multigrep/actions?query=workflow%3Alint) | [![Dependencies](https://github.com/clj-commons/multigrep/workflows/dependencies/badge.svg?branch=dev)](https://github.com/clj-commons/multigrep/actions?query=workflow%3Adependencies) |

[![Latest Version](https://img.shields.io/clojars/v/clj-commons/multigrep)](https://clojars.org/clj-commons/multigrep/) [![Open Issues](https://img.shields.io/github/issues/clj-commons/multigrep.svg)](https://github.com/clj-commons/multigrep/issues) [![License](https://img.shields.io/github/license/clj-commons/multigrep.svg)](https://github.com/clj-commons/multigrep/blob/main/LICENSE)

# multigrep

A little Clojure library that provides regex-based file grepping and/or text substitution.

## Installation

multigrep is available as a Maven artifact from [Clojars](https://clojars.org/clj-commons/multigrep).

### Trying it Out

#### Clojure CLI

```shell
$ clj -Sdeps '{:deps {clj-commons/multigrep {:mvn/version "#.#.#"}}}'  # Where #.#.# is replaced with an actual version number (see badge above)
```

#### Leiningen

```shell
$ lein try clj-commons/multigrep
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

The library provides two functions - `grep` (for searching for text within files) and (since v0.3.0) `greplace!` (for searching and replacing text within files).
[The API documentation](https://clj-commons.github.io/multigrep/) has full details, and [the unit tests](https://github.com/clj-commons/multigrep/blob/master/test/multigrep/core_test.clj) have comprehensive examples.

The [cljdoc](https://cljdoc.org/) project also publishes [up-to-date documentation for this project](https://cljdoc.org/d/clj-commons/multigrep/CURRENT).

### API Documentation

[API documentation is available here](http://clj-commons.org/multigrep/).

## Contributor Information

[Contributing Guidelines](https://github.com/clj-commons/multigrep/blob/main/.github/CONTRIBUTING.md)

[Bug Tracker](https://github.com/clj-commons/multigrep/issues)

[Code of Conduct](https://github.com/clj-commons/multigrep/blob/main/.github/CODE_OF_CONDUCT.md)

### Developer Workflow

The repository has two permanent branches: `main` and `dev`.  **All development must occur either in branch `dev`, or (preferably) in feature branches off of `dev`.**  All PRs must also be submitted against `dev`; the `main` branch is **only** updated from `dev` via PRs created by the core development team.  All other changes submitted to `main` will be rejected.

This model allows otherwise unrelated changes to be batched up in the `dev` branch, integration tested there, and then released en masse to the `main` branch, which will trigger automated generation and deployment of the release (Codox docs to GitHub Pages, JARs to Clojars, etc.).

## License

Copyright © 2014 Peter Monks

Distributed under the [Eclipse Public License](http://www.eclipse.org/legal/epl-v20.html) either version 2.0 or (at your option) any later version.

SPDX-License-Identifier: [EPL-2.0](https://spdx.org/licenses/EPL-2.0)
