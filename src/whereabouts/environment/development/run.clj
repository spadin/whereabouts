(ns whereabouts.environment.development.run
  (:require [whereabouts.server :as server]
            [whereabouts.config             :refer [set-env!]]
            [ring.adapter.jetty             :refer [run-jetty]]
            [ring.middleware.json           :refer [wrap-json-response wrap-json-body]]
            [ring.middleware.params         :refer [wrap-params]]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [ring.middleware.reload         :refer [wrap-reload]]
            [ring.middleware.stacktrace     :refer [wrap-stacktrace]]))

(def development-handler
  (-> server/handler
      wrap-json-response
     (wrap-json-body {:keywords? true})
      wrap-keyword-params
      wrap-params
      wrap-reload
      wrap-stacktrace))

(defn -main [& args]
  (set-env! :development)
  (let [port (server/get-port (first args))]
    (run-jetty development-handler {:port port})))
