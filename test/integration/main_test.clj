(ns main-test
  (:require
   [clojure.test :refer [deftest is use-fixtures testing]]
   [fixtures :refer [with-web-server]]
   [clj-http.client :as http]))

(use-fixtures :once with-web-server)

(deftest web-server-test
  (testing "Check web server response"
    (let [{:keys [status _body error] :as _resp} (http/get "http://localhost:8080")]
      (is (= 200 status))
      (is (= nil error)))))