(ns whereabouts.config
  (:require [whereabouts.server                 :as server])
  (:require [whereabouts.database.elasticsearch :as elasticsearch]))

(defn eval-file [f]
  (read-string (slurp f)))

(defn load-config-map [env]
  (eval-file (str "src/whereabouts/environment/" (name env) "/config.edn")))

(defn set-env! [env]
  (let [config (load-config-map env)]
    (server/setup! (:server config))
    (elasticsearch/setup! (:elasticsearch config))))
