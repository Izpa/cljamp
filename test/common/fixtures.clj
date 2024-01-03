(ns fixtures
  (:require
   [server :refer [start!]]
   [taoensso.timbre :as log]))

(defn with-web-server
  [f]
  (time
   (let [server (start! nil)]
     (log/debug "Time of running all tests per fixture:")
     (time (f))
     (.stop server)
     (log/debug "Total time of running tests + system up/down:"))))