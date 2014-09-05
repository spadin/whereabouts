(ns whereabouts.server
  (:require [compojure.core                       :refer [context defroutes GET]]
            [whereabouts.marker.marker-controller :refer [marker-routes]]))

(def default-port (atom nil))

(defn setup! [config]
  (reset! default-port (:default-port config)))

(defn get-port [port-str-or-nil]
  (let [port-str (str (or port-str-or-nil @default-port))]
    (if-not (empty? port-str)
      (Integer/parseInt port-str))))

(defroutes handler
  (context "/marker" [] marker-routes)
  (GET "/" [] ""))
