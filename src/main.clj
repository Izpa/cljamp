(ns main
  (:require [org.httpkit.server :as hk-server])
  (:gen-class))

(defn hello-world
  []
  "Hello world!")

(defn app [_req]
  {:status  200
   :headers {"Content-Type" "text/html"}
   :body    "hello HTTP!"})

(defn run []
  (hk-server/run-server app {:port 3000}))

(defn -main [& _args]
  (run))