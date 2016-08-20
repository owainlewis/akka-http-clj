;; Copyright (c) 2016 Owain Lewis <owain@owainlewis.com>
;; All rights reserved.
;;
;; Redistribution and use in source and binary forms, with or without
;; modification, are permitted provided that the following conditions
;; are met:
;;
;; 1. Redistributions of source code must retain the above copyright
;;    notice, this list of conditions and the following disclaimer.
;; 2. Redistributions in binary form must reproduce the above copyright
;;    notice, this list of conditions and the following disclaimer in the
;;    documentation and/or other materials provided with the distribution.
;;
;; THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
;; IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
;; OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
;; IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
;; INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
;; NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
;; DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
;; THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
;; (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
;; THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
(ns akka-http-clj.client
  (:refer-clojure :exclude [get])
  (:import (io.forward.akka.http.client InternalRequest Client)
           (java.util.function Function))
  (:require [clojure.string :as str]))

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
  "Issues an async HTTP request and returns a Java8 CompletableFuture."
  [req]
  (let [internal-request (map->internalRequest req)
        start-time (. System (nanoTime))
        response (.run client internal-request)]
    (fmap response
      (fn [future-response]
        (let [response-meta {:response-time (/ (double (- (. System (nanoTime)) start-time)) 1000000.0)}]
          (with-meta
            { :status (.getStatusCode future-response)
              :body (.getBody future-response)
              :headers (into {} (.getHeaders future-response)) }
            response-meta))))))

(defmacro defreq
  [method]
  `(defn ~method
     ~(str "Issues an async HTTP " (clojure.string/upper-case method) " request")
     ~'[url & req]
     (request (merge {:method :get :url ~'url}
                     (into {} ~'req)))))

(defreq get)
(defreq put)
(defreq post)
(defreq delete)
(defreq patch)
