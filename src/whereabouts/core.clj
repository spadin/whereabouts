(ns whereabouts.core
  (:require [compojure.core                       :refer [context defroutes GET]]
            [whereabouts.marker.marker-controller :refer [marker-routes]]))

(defroutes handler
  (context "/marker" [] marker-routes)
  (GET "/" [] ""))
