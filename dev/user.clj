(ns user) ; Must be ".clj" file, Clojure doesn't auto-load user.cljc

; lazy load dev stuff - for faster REPL startup and cleaner dev classpath
(def start-electric-server! (delay @(requiring-resolve 'server/start!)))
(def shadow-start! (delay @(requiring-resolve 'shadow.cljs.devtools.server/start!)))
(def shadow-watch (delay @(requiring-resolve 'shadow.cljs.devtools.api/watch)))

(def electric-server-config
  {:host "0.0.0.0", :port 8080, :resources-path "public"})

(defn main [& _args]
  (println "Starting Electric compiler and server...")
  (@shadow-start!) ; serves index.html as well
  (@shadow-watch :dev) ; depends on shadow server
  ; Shadow loads view.todo-list here, such that it shares memory with server. 
  #_{:clj-kondo/ignore [:inline-def]}
  (def server (@start-electric-server! electric-server-config))
  (comment (.stop server)))

; Server-side Electric userland code is lazy loaded by the shadow build.
; WARNING: make sure your REPL and shadow-cljs are sharing the same JVM!

(comment
  (main) ; Electric Clojure(JVM) REPL entrypoint
  #_{:clj-kondo/ignore [:unresolved-namespace]}
  (hyperfiddle.rcf/enable!) ; turn on RCF after all transitive deps have loaded
  #_{:clj-kondo/ignore [:unresolved-namespace]}
  (shadow.cljs.devtools.api/repl :dev) ; shadow server hosts the cljs repl
  ; connect a second REPL instance to it
  ; (DO NOT REUSE JVM REPL it will fail weirdly)
  (type 1))