(ns whereabouts.environment.development.run
  (:require [whereabouts.core :as core]
            [whereabouts.config         :refer [config-for set-env!]]
            [ring.adapter.jetty         :refer [run-jetty]]
            [ring.middleware.params     :refer [wrap-params]]
            [ring.middleware.reload     :refer [wrap-reload]]
            [ring.middleware.stacktrace :refer [wrap-stacktrace]]))

(def development-handler
  (-> core/handler
      wrap-params
      wrap-reload
      wrap-stacktrace))

(defn- get-port [args]
  (let [command-line-port (first args)
        default-port      (config-for :server :port)
        port-str          (str (or command-line-port default-port))]
    (Integer/parseInt port-str)))

(defn -main [& args]
  (set-env! :development)
  (let [port (get-port args)]
    (run-jetty development-handler {:port port})))
