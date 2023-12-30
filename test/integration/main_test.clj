(ns main-test
  (:require
   [clojure.test :refer [deftest is use-fixtures testing]]
   [fixtures :refer [with-web-server]]
   [org.httpkit.client :as http]))

(use-fixtures :once with-web-server)

(deftest web-server-test
  (testing "Check web server response"
    (let [{:keys [status body error] :as _resp} @(http/get "http://localhost:3000")]
      (is (= 200 status))
      (is (= "hello HTTP!" body))
      (is (= nil error)))))