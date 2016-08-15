# akka-http-clj

As a Scala developer I found the Akka HTTP stuff really great to work with (fast, easy and performant).

This is a HTTP client for Clojure that exposes the power of Akka.

## Usage

```clojure
(ns myns
  (require [akka-http-clj.client :as http]))

(request {:method :get
          :uri "http://owainlewis.com"})

```

## License

Copyright © 2016 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
