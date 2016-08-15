(ns akka-http-clj.client
  (:import [io.forward.akka.http.client InternalRequest InternalResponse Client]))

(defn map->internalRequest
  "Given a request valid request map will return an internal request to be
   dispatched"
  [{:keys [method uri headers]}]
  (InternalRequest. (name method) uri))

(defn request
  "Given a HTTP request, it will dispatch the request and return a future result.
   It's worth being aware that this returns a Java8 CompletableFuture."
  [req]
  (let [internal-request (map->internalRequest req)
        client (Client.)]
    (.runRequest client request)))
