(ns akka-http-clj.client
  (:import (io.forward.akka.http.client InternalRequest Client)
           (java.util.function Function)))

(def client
  "A single instance of the internal HTTP client.
   Because options probably need setting here for config and performance
   I'm assuming it will eventually be possible to pass in a custom client"
  (Client.))

(defn map->internalRequest
  "Given a request valid request map will return an internal request to be
   dispatched"
  [{:keys [method uri headers body]}]
  (let [req-method (name method)
        req-headers (java.util.HashMap. headers)]
  (InternalRequest. 
    req-method 
    uri 
    req-headers 
    body)))

(defn fmap
  "Map some function f over a CompletableFuture cf"
  [cf f]
  (.thenApplyAsync cf
    (reify Function
      (apply [_ v] (f v)))))

(defn request
  "Given a HTTP request, it will dispatch the request and return a response as a Clojure map.
   Because we're working with CompletableFutures we use the Java8 API to map over the
   future repsponse and transform it"
  [req]
  (let [internal-request (map->internalRequest req)
        start-time (. System (nanoTime))
        response (.run client internal-request)]
    (fmap response
      (fn [future-response]
        (let [response-meta {:response-time (/ (double (- (. System (nanoTime)) start-time)) 1000000.0)}]
          (with-meta
            { :status (.getStatusCode future-response)
              :headers (into {} (.getHeaders future-response)) }
            response-meta))))))