(ns whereabouts.database.elasticsearch
  (:require [clojurewerkz.elastisch.rest          :as esr]
            [clojurewerkz.elastisch.rest.document :as esd]))

(def uri   (atom nil))
(def index (atom nil))

(defn setup! [config]
  (reset! uri   (:uri   config))
  (reset! index (:index config)))

(defn connect
  ([]    (connect @uri))
  ([uri] (esr/connect uri)))

(defn prepare-response [id doc]
  (assoc doc :id id))

(defn get-doc
  ([conn index type id]
   (let [resp (esd/get conn index type id)]
     (prepare-response id (:_source resp))))
  ([conn type id]
   (get-doc conn @index type id))
  ([type id]
   (get-doc (connect) type id)))

(defn set-doc
  ([conn index type id doc]
   (esd/put conn index type id doc)
   (prepare-response id doc))
  ([conn type id doc]
   (set-doc conn @index type id doc))
  ([type id doc]
   (set-doc (connect) type id doc)))
