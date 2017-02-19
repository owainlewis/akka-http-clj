(ns akka-http-clj.types)

(defrecord HttpRequest
  [method url headers body])

(defrecord HttpResponse
  [status headers body])
