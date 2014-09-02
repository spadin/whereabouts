(ns whereabouts.environment.production.run
  (:require [whereabouts.core   :as core]
            [whereabouts.server :as server]
            [whereabouts.config         :refer [set-env!]]
            [ring.adapter.jetty         :refer [run-jetty]]
            [ring.middleware.params     :refer [wrap-params]]
            [ring.middleware.reload     :refer [wrap-reload]]
            [ring.middleware.stacktrace :refer [wrap-stacktrace]]))

(def production-handler
  (-> core/handler
      wrap-params
      wrap-reload
      wrap-stacktrace))

(defn- get-port [args]
  (let [command-line-port (first args)
        default-port      @server/default-port
        port-str          (str (or command-line-port default-port))]
    (Integer/parseInt port-str)))

(defn -main [& args]
  (set-env! :production)
  (let [port (get-port args)]
    (run-jetty production-handler {:port port})))
