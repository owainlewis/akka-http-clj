# akka-http-clj

Akka HTTP is a fast, reliable and asynchronous HTTP client from the Scala world. 
It uses [reactive streams](http://www.reactive-streams.org/) to implement TCP backpressure.

This library implements a Clojure interface for using Akka HTTP (along with the powerful of Reactive Streams) that closely
mimics existing Clojure HTTP clients.

## Usage

```clojure
(ns myns
  (require [akka-http-clj.client :as http]))

(defn example-request []
  (let [req {:method :post
             :uri "http://requestb.in/1mpaovf1"
             :headers {"X-Foo", "Bar"}
             :body "Hello World"}
        response @(request req)]
  (println response)
  ;; Meta data contains useful information like repsonse times :)
  (println (meta response)))
```

## License

Copyright © 2016 Owain Lewis

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
