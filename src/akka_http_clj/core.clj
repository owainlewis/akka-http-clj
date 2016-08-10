(ns akka-http-clj.core
  (:import [akka.stream ActorMaterializer]
           [akka.actor ActorSystem]))

;; TODO maybe do this in Java and pass in the mappings only from CLJ->Request and Response objects?

(defn actor-system
  "Create a new Akka ActorSystem"
  []
  (ActorSystem/create))

(defn actor-materializer
  "Construct an actor materializer"
  [^ActorSystem system]
  (ActorMaterializer/create system))
