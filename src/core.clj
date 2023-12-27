(ns core
  (:require [org.httpkit.server :as hk-server]))

(defn hello-world
  []
  "Hello world!")

(defn app [req]
  {:status  200
   :headers {"Content-Type" "text/html"}
   :body    "hello HTTP!"})

(def my-server (hk-server/run-server app {:port 8888}))