(ns whereabouts.config
  (:require [whereabouts.server                 :as server])
  (:require [whereabouts.database.elasticsearch :as elasticsearch]))

(def ^:dynamic *env*    (atom nil))
(def ^:dynamic *config* (atom {}))

(defn eval-file [f]
  (read-string (slurp f)))

(defn load-config-map [env]
  (eval-file (str "src/whereabouts/environment/" (name env) "/config.edn")))

(defn load-config! []
  (reset! *config* (load-config-map @*env*)))

(defn config-for [& k]
  (get-in @*config* k))

(defn set-env! [new-env]
  (reset! *env* new-env)
  (load-config!)
  (server/setup! (:server @*config*))
  (elasticsearch/setup! (:elasticsearch @*config*)))
