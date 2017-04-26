# akka-http-clj

Akka HTTP provides a fast, reliable and asynchronous HTTP client. 

It uses [reactive streams](http://www.reactive-streams.org/) to implement TCP backpressure.

This library implements a ring like DSL for using Akka HTTP (along with the power of Reactive Streams) that closely
mimics existing Clojure HTTP clients.

## TODO

- [ ] Request Entities (content types are set different in Akka HTTP)
- [ ] {:as :json} Set content types
- [ ] Configuration options for tuning the client
- [ ] Better docs
- [ ] Proxy and TLS configurable

## Usage

```clojure
(ns myns
  (require [akka-http-clj.client :as http]))

(defn example-request []
  (let [req {:method :post
             :uri "http://requestb.in/1mpaovf1"
             :headers {"X-Foo", "Bar"}
             :body "Hello World"}
        response @(http/request req)]
  (println response)
  ;; Meta data contains useful information like repsonse times :)
  (println (meta response)))
```

## Making Requests

The request API mimics the http-kit and clj-http APIs as closely as possible.

GET request

```clojure
@(client/get "http://example.com")
```

GET request with headers

```clojure
@(client/get "http://example.com" {:headers {"X-Foo" "Bar"}})
```

POST request

```clojure
@(client/get "http://example.com"
    {:body "Hello, World!"
     :headers {"X-Foo" "Bar"}})
```

## Timing HTTP requests

The response time is automatically available in the response meta


```clojure
(->> @(get "http://github.com/owainlewis")
                           (meta)
                           (:response-time))
```

## License

Copyright © 2016 Owain Lewis

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
