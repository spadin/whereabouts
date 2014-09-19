(ns whereabouts.marker.marker-controller
  (:require [compojure.core     :refer [defroutes GET POST]]
            [ring.util.response :refer [response]]
            [whereabouts.elasticsearch.elasticsearch :as elasticsearch]))

(defn get-marker [id]
  (elasticsearch/get-doc "marker" id))

(defn set-marker [id marker]
  (elasticsearch/set-doc "marker" id marker))

(defn search [bounding-box]
  (elasticsearch/search-location "marker" bounding-box))

(defn- int-param [params k]
  (Integer/parseInt (params k)))

(defn- bounding-box-from-params [params]
  {:top_left     {:lat (int-param params :top_left_lat)
                  :lon (int-param params :top_left_lon)}
   :bottom_right {:lat (int-param params :bottom_right_lat)
                  :lon (int-param params :bottom_right_lon)}})

(defroutes marker-handler
  (POST "/search-location" [:as {body :body}]    (response (search body)))
  (GET  "/search-location" [& params]            (response (search (bounding-box-from-params params))))
  (GET  "/:id"             [id]                  (response (get-marker id)))
  (POST "/:id"             [id :as {body :body}] (response (set-marker id body))))
