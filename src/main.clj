(ns main
  (:gen-class)
  (:require view.todo-list ; main only (app is not loaded by shadow in main)
            clojure.string
            server))

(def electric-server-config
  {:host "0.0.0.0", :port 8080, :resources-path "public"})

(defn -main [& _args] ; run with `clj -M -m main`
  (when (clojure.string/blank? (System/getProperty "HYPERFIDDLE_ELECTRIC_SERVER_VERSION"))
    (throw (ex-info "HYPERFIDDLE_ELECTRIC_SERVER_VERSION jvm property must be set in main" {})))
  (server/start-server! electric-server-config))

; On CLJS side we reuse src/user.cljs for main entrypoint