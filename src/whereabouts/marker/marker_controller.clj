(ns whereabouts.marker.marker-controller
  (:require [compojure.core     :refer [defroutes GET POST]]
            [ring.util.response :refer [response]]
            [whereabouts.database.elasticsearch :as elasticsearch]))

(defn get-marker [id]
  (elasticsearch/get-doc :marker id))

(defn set-marker [id marker]
  (elasticsearch/set-doc :marker id marker))

(defroutes marker-handler
  (GET  "/:id" [id]                  (response (get-marker id)))
  (POST "/:id" [id :as {body :body}] (response (set-marker id body))))
