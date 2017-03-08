(ns akka-http-clj.client
  (:refer-clojure :exclude [get])
  (:import (io.forward.akka.http.client InternalRequest Client)
           (java.util.function Function))
  (:require [akka-http-clj.types :refer [HttpRequest HttpResponse]]
            [clojure.string :as str]))

(def client
  "A single instance of the internal HTTP client.
   Because options probably need setting here for config and performance
   I'm assuming it will eventually be possible to pass in a custom client"
  (Client.))

(defn map->internalRequest
  "Given a request valid request map will return an internal request to be
   dispatched"
  [{:keys [url method headers body]}]
  (let [req-method (name method)
        req-headers (java.util.HashMap. (or headers {}))]
    (if (nil? body)
      (InternalRequest. req-method url req-headers)
      (InternalRequest. req-method url req-headers body))))

(defn fmap
  "Map some function f over a CompletableFuture cf"
  [cf f]
  (.thenApply cf
    (reify Function
      (apply [_ v] (f v)))))

(defn request
  "Issues an async HTTP request and returns a Future response"
  [req]
  (let [internal-request (map->internalRequest req)
        start-time (. System (nanoTime))
        response (.run client internal-request)]
    (fmap response
      (fn [future-response]
        (let [response-meta
               {:response-time
                 (/ (double (- (. System (nanoTime)) start-time)) 1000000.0)}]
          (with-meta
            (let [status (.getStatusCode future-response)
                  body (.getBody future-response)
                  hdrs (into {} (.getHeaders future-response))]
              (HttpResponse. status headers body)
            response-meta)))))))

(defmacro defreq
  [method]
  `(defn ~method
     ~(str "Issues an async HTTP " (clojure.string/upper-case method) " request")
     ~'[url & req]
     (request (merge {:method ~(keyword method) :url ~'url}
                     (into {} ~'req)))))

(defreq get)
(defreq put)
(defreq post)
(defreq delete)
(defreq patch)
