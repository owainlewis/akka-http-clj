(ns akka-http-clj.core-test
  (:require [clojure.test :refer :all]
            [akka-http-clj.client :refer :all]))

(defn run-request []
  (println "Making request")
  (let [req {:method :post
             :uri "http://requestb.in/pw9e33pw"
             :headers {"X-Foo", "Bar"}
             :body "Hello World"}
        response (request req)]
    (fmap response
          (fn [r]
            (println r)))))

(deftest request-test
  (testing "It works"
    (let [futures (doall (map (fn [_] (run-request)) (range 100)))]
      (while true
        ))))
