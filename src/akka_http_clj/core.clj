(ns akka-http-clj.core
  (:import java.util.function.Function
           [io.forward.akka.http.client InternalRequest InternalResponse Client]))

(defrecord HRequest [method uri headers])

(def request-example
  (map->HRequest
    {:method :get
     :uri "http://owainlewis.com"
     :headers {}}))

(defn map->internalRequest [{:keys [method uri headers]}]
  (InternalRequest. (name method) uri))

(defn run-request
  "Example HTTP request"
  [request]
  (let [client (Client.)]
    (.runRequest client request)))

(defn- reify-function
  "Helper to make it easy to map clojure functions to java.util.function.Function"
  [f]
  (reify Function
    (apply [_ v] (f v))))
4
(defn- fmap
  "Map some function f over a CompletableFuture cf"
  [cf f]
  (.thenApplyAsync cf (reify-function f)))

(defn demo
  []
  (let [response (run-request)]
    (fmap response
          (fn [r]
            (println r)))))

(defn -main []
  (println "Running request")
  (demo)
  (shutdown-agents))
