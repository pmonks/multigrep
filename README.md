| | | |
|---:|:---:|:---:|
| [**main**](https://github.com/pmonks/multigrep/tree/main) | [![CI](https://github.com/pmonks/multigrep/workflows/CI/badge.svg?branch=main)](https://github.com/pmonks/multigrep/actions?query=workflow%3ACI+branch%3Amain) | [![Dependencies](https://github.com/pmonks/multigrep/workflows/dependencies/badge.svg?branch=main)](https://github.com/pmonks/multigrep/actions?query=workflow%3Adependencies+branch%3Amain) |
| [**dev**](https://github.com/pmonks/multigrep/tree/dev) | [![CI](https://github.com/pmonks/multigrep/workflows/CI/badge.svg?branch=dev)](https://github.com/pmonks/multigrep/actions?query=workflow%3ACI+branch%3Adev) | [![Dependencies](https://github.com/pmonks/multigrep/workflows/dependencies/badge.svg?branch=dev)](https://github.com/pmonks/multigrep/actions?query=workflow%3Adependencies+branch%3Adev) |

[![Latest Version](https://img.shields.io/clojars/v/com.github.pmonks/multigrep)](https://clojars.org/com.github.pmonks/multigrep/) [![Open Issues](https://img.shields.io/github/issues/pmonks/multigrep.svg)](https://github.com/pmonks/multigrep/issues) [![License](https://img.shields.io/github/license/pmonks/multigrep.svg)](https://github.com/pmonks/multigrep/blob/main/LICENSE)

# multigrep

A little Clojure library that provides regex-based file grepping and/or text substitution.

## Installation

`multigrep` is available as a Maven artifact from [Clojars](https://clojars.org/com.github.pmonks/multigrep).

### Trying it Out

#### Clojure CLI

```shell
$ clj -Sdeps '{:deps {com.github.pmonks/multigrep {:mvn/version "RELEASE"}}}'
```

#### deps-try

```shell
$ deps-try com.github.pmonks/multigrep
```

#### Leiningen

```shell
$ lein try com.github.pmonks/multigrep
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

This project uses the [git-flow branching strategy](https://nvie.com/posts/a-successful-git-branching-model/), with the caveat that the permanent branches are called `main` and `dev`, and any changes to the `main` branch are considered a release and auto-deployed (JARs to Clojars, API docs to GitHub Pages, etc.).

For this reason, **all development must occur either in branch `dev`, or (preferably) in temporary branches off of `dev`.**  All PRs from forked repos must also be submitted against `dev`; the `main` branch is **only** updated from `dev` via PRs created by the core development team.  All other changes submitted to `main` will be rejected.

### Why are there so many different groupIds on Clojars for this project?

The project was originally developed under my personal GitHub account.  In early 2018 it was transferred to the `clj-commons` GitHub organisation, but then, as that group refined their scope and mission, it was determined that it no longer belonged there, and the project were transferred back in late 2021.  During this time the build tooling for the project also changed from Leiningen to tools.build, which created further groupId churn (tools.build introduced special, useful semantics for `com.github.username` groupIds that don't exist with Leiningen or Clojars).

## License

Copyright © 2014 Peter Monks

Distributed under the [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0).

SPDX-License-Identifier: [Apache-2.0](https://spdx.org/licenses/Apache-2.0)
