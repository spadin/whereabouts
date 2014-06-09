(ns whereabouts.marker.marker-controller
  (:require [compojure.core :refer [defroutes GET POST]]
            [cheshire.core  :refer [generate-string parse-string]]
            [whereabouts.database.elasticsearch :as es]))

(defn find-marker [id]
  (es/get-doc "marker" id))

(defn set-marker [body]
  (let [marker (parse-string (slurp body) true)
        id     (:id marker)
        doc    (dissoc marker :id)]
    (es/set-doc "marker" id doc)))

(defn render-json [obj]
  {:status  200
   :headers {"Content-type" "application/json"}
   :body    (generate-string obj)})

(defn parse-int [string]
  (Integer/parseInt string))

(defroutes marker-routes
  (GET  "/:id" [id] (render-json (find-marker id)))
  (POST "/"    {:keys [body]}   (render-json (set-marker body)))
  (GET  "/"    {:keys [params]} (render-json {:hello :world})))
