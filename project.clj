(defproject io.forward/akka-http-clj "0.1.0-SNAPSHOT"
  :description "A high performance HTTP client using Akka"
  :url "http://github.com/owainlewis/akka-http-clj"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :java-source-paths ["src/java"]
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [com.typesafe.akka/akka-actor_2.11 "2.5.0"]
                 [com.typesafe.akka/akka-stream_2.11 "2.5.0"]
                 [com.typesafe.akka/akka-http-experimental_2.11 "2.5.0"]
                 [com.typesafe.akka/akka-http-core_2.11 "2.5.0"]])
