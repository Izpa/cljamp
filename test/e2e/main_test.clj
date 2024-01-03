(ns main-test
  (:require
   [clojure.test :refer [deftest is use-fixtures]]
   [fixtures :refer [with-web-server]]
   [common :as c]
   [etaoin.api :as e]))

(use-fixtures :each with-web-server c/with-driver)

(deftest main-page-test
  (c/testing "Check content on main page"
    (e/go c/*driver* "http://localhost:8080")
    (is (e/wait-has-text c/*driver* "minimal todo list"))))