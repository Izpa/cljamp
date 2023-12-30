(ns fixtures
  (:require
   [main :refer [run-server stop-server]]
   [taoensso.timbre :as log]))

(defn with-web-server
  [f]
  (time
   (do
     (run-server)
     (log/debug "Time of running all tests per fixture:")
     (time (f))
     (stop-server)
     (log/debug "Total time of running tests + system up/down:"))))