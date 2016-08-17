(ns akka-http-clj.core-test
  (:require [clojure.test :refer :all]
            [akka-http-clj.client :refer :all]))

(deftest request-test
  (testing "It works"
      (let [req {:method :post
                 :uri "http://requestb.in/1mpaovf1"
                 :headers {"X-Foo", "Bar"}
                 :body "Hello World"}
            response @(request req)]
        (println response)
        (println (meta response)))))
