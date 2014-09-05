(ns whereabouts.environment.production.run
  (:require [whereabouts.server :as server]
            [whereabouts.config         :refer [set-env!]]
            [ring.adapter.jetty         :refer [run-jetty]]
            [ring.middleware.params     :refer [wrap-params]]))

(def production-handler
  (-> server/handler
      wrap-params))

(defn -main [& args]
  (set-env! :production)
  (let [port (server/get-port (first args))]
    (run-jetty production-handler {:port port})))
