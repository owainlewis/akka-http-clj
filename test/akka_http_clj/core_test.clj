(ns akka-http-clj.core-test
  (:require [clojure.test :refer :all]
            [akka-http-clj.client :as client]))

(defonce endpoint "http://requestb.in/ssc154ss")

(deftest get-requests
  (testing "It should send a HTTP GET request"
    (let [response @(client/get endpoint {:headers {"X-Test" "simple-get"}})]
      (is (= 200 (:status response))))))

(deftest post-requests
  (testing "It should send a HTTP POST request"
    (let [response @(client/post endpoint
                                 {:body "Hello, World"
                                  :headers {"X-Test" "simple-post"}})]
      (println response)
      (is (= 200 (:status response))))))
