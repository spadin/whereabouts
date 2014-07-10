(ns whereabouts.environment.development.run
  (:require [whereabouts.core :as core]
            [ring.adapter.jetty         :refer [run-jetty]]
            [ring.middleware.params     :refer [wrap-params]]
            [ring.middleware.reload     :refer [wrap-reload]]
            [ring.middleware.stacktrace :refer [wrap-stacktrace]]))

(def development-handler
  (-> core/handler
      wrap-params
      wrap-reload
      wrap-stacktrace))

(def config-map
  (read-string (slurp "src/whereabouts/environment/development/config.edn")))

(defn- config-for [& k]
  (get-in config-map k))

(defn- get-port [args]
  (Integer/parseInt (or (first args) (str (config-for :server :port)))))

(defn -main [& args]
  (let [port (get-port args)]
    (run-jetty development-handler {:port port})))
