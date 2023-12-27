(ns core-test
  (:require
   [core :as sut]
   [clojure.test :refer [deftest is testing]]))

(deftest hello-world-test
  (testing "Simple test example"
    (is (= "Hello world!"
           (sut/hello-world)))))