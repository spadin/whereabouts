(ns whereabouts.environment.development
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

(defn -main [& args]
  (let [port (Integer/parseInt (or (first args) "3000"))]
    (run-jetty development-handler {:port port})))
