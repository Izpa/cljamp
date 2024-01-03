(ns build
  "build electric.jar library artifact and demos"
  (:require [clojure.tools.build.api :as b]
            [shadow.cljs.devtools.api :as shadow-api] ; so as not to shell out to NPM for shadow
            [shadow.cljs.devtools.server :as shadow-server]))

(def lib 'izpa/cljamp)
(def version (format "0.0.%s" (b/git-count-revs nil)))
(def class-dir "target/classes")
(def basis (b/create-basis {:project "deps.edn"}))
(def uber-file "target/cljamp.jar")

(defn clean [_]
  (b/delete {:path "target"}))

(defn clean-cljs [_]
  (b/delete {:path "resources/public/js"}))

(defn build-client
  "Main optimized ClojureScript client build. (Note: in dev, the client is built 
on startup)"
  [{:keys [optimize debug verbose version]
    :or {optimize true, debug false, verbose false, version version}}]
  (println "Building client. Version:" version)
  (shadow-server/start!)
  (shadow-api/release :main {:debug debug,
                             :verbose verbose,
                             :config-merge [{:compiler-options {:optimizations (if optimize :advanced :simple)}
                                             :closure-defines {'hyperfiddle.electric-client/VERSION version}}]})
  (shadow-server/stop!))

(defn uber [{:keys [version optimize debug verbose]
             :or   {version version, optimize true, debug false, verbose false}}]
  (println "Cleaning up before build")
  (clean nil)
  
  (println "Cleaning cljs compiler output")
  (clean-cljs nil)
  
  (build-client {:optimize optimize
                 :debug    debug
                 :verbose  verbose
                 :version  version})

  (println "Bundling sources")
  (b/copy-dir {:src-dirs ["src" "resources"]
               :target-dir class-dir})
  
  (println "Compiling server. Version:" version)
  (b/compile-clj {:basis      basis
                  :src-dirs   ["src"]
                  :sort       :bfs
                  :ns-compile '[main]
                  :class-dir  class-dir})
  
  (println "Building uberjar")
  (b/uber {:class-dir class-dir
           :uber-file uber-file
           :basis     basis
           :main      'main}))