| | | |
|---:|:---:|:---:|
| [**main**](https://github.com/pmonks/multigrep/tree/main) | [![CI](https://github.com/pmonks/multigrep/workflows/CI/badge.svg?branch=main)](https://github.com/pmonks/multigrep/actions?query=workflow%3ACI) | [![Dependencies](https://github.com/pmonks/multigrep/workflows/dependencies/badge.svg?branch=main)](https://github.com/pmonks/multigrep/actions?query=workflow%3Adependencies) |
| [**dev**](https://github.com/pmonks/multigrep/tree/dev)  | [![CI](https://github.com/pmonks/multigrep/workflows/CI/badge.svg?branch=dev)](https://github.com/pmonks/multigrep/actions?query=workflow%3ACI) | [![Dependencies](https://github.com/pmonks/multigrep/workflows/dependencies/badge.svg?branch=dev)](https://github.com/pmonks/multigrep/actions?query=workflow%3Adependencies) |

[![Latest Version](https://img.shields.io/clojars/v/com.github.pmonks/multigrep)](https://clojars.org/com.github.pmonks/multigrep/) [![Open Issues](https://img.shields.io/github/issues/pmonks/multigrep.svg)](https://github.com/pmonks/multigrep/issues) [![License](https://img.shields.io/github/license/pmonks/multigrep.svg)](https://github.com/pmonks/multigrep/blob/main/LICENSE)

# multigrep

A little Clojure library that provides regex-based file grepping and/or text substitution.

## Installation

`multigrep` is available as a Maven artifact from [Clojars](https://clojars.org/com.github.pmonks/multigrep).

### Trying it Out

#### Clojure CLI

```shell
$ clj -Sdeps '{:deps {com.github.pmonks/multigrep {:mvn/version "#.#.#"}}}'  # Where #.#.# is replaced with an actual version number (see badge above)
```

#### Leiningen

```shell
$ lein try com.github.pmonks/spinner
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

The library provides two functions - `grep` (for searching for text within files) and `greplace!` (for searching and replacing text within files).
[The API documentation](https://pmonks.github.io/multigrep/) has full details, and [the unit tests](https://github.com/pmonks/multigrep/blob/main/test/multigrep/core_test.clj) contain comprehensive usage examples.

### API Documentation

[API documentation is available here](https://pmonks.github.io/multigrep/).

## Contributor Information

[Contributing Guidelines](https://github.com/pmonks/multigrep/blob/main/.github/CONTRIBUTING.md)

[Bug Tracker](https://github.com/pmonks/multigrep/issues)

[Code of Conduct](https://github.com/pmonks/multigrep/blob/main/.github/CODE_OF_CONDUCT.md)

### Developer Workflow

The repository has two permanent branches: `main` and `dev`.  **All development must occur either in branch `dev`, or (preferably) in feature branches off of `dev`.**  All PRs must also be submitted against `dev`; the `main` branch is **only** updated from `dev` via PRs created by the core development team.  All other changes submitted to `main` will be rejected.

This model allows otherwise unrelated changes to be batched up in the `dev` branch, integration tested there, and then released en masse to the `main` branch, which will trigger automated generation and deployment of the release (Codox docs to GitHub Pages, JARs to Clojars, etc.).

### Why are there so many different groupIds on Clojars for this project?

The project was originally developed under my personal GitHub account.  In early 2018 it was transferred to the `clj-commons` GitHub organisation, but then, as that group refined their scope and mission, it was determined that it no longer belonged there, and the project were transferred back in late 2021.  During this time the build tooling for the project also changed from Leiningen to tools.build, which created further groupId churn (tools.build introduced special, useful semantics for `com.github.username` groupIds that don't exist with Leiningen or Clojars).

## License

Copyright © 2014 Peter Monks

Distributed under the [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0).

SPDX-License-Identifier: [Apache-2.0](https://spdx.org/licenses/Apache-2.0)
