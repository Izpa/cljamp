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

(defonce server (atom nil))

(defn stop-server []
  (when-not (nil? @server)
    ;; graceful shutdown: wait 100ms for existing requests to be finished
    ;; :timeout is optional, when no timeout, stop immediately
    (@server :timeout 100)
    (reset! server nil)))

(defn run-server []
  (reset! server (hk-server/run-server #'app {:port 3000})))

(defn -main [& _args]
  (run-server))