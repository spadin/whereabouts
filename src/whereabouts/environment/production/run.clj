(ns whereabouts.environment.production.run
  (:require [whereabouts.server :as server]
            [whereabouts.config             :refer [set-env!]]
            [ring.adapter.jetty             :refer [run-jetty]]
            [ring.middleware.json           :refer [wrap-json-response wrap-json-body]]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [ring.middleware.params         :refer [wrap-params]]))

(def production-handler
  (-> server/handler
      wrap-json-response
     (wrap-json-body {:keywords? true})
      wrap-keyword-params
      wrap-params))

(defn -main [& args]
  (set-env! :production)
  (let [port (server/get-port (first args))]
    (run-jetty production-handler {:port port})))
