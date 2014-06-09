(ns whereabouts.database.elasticsearch
  (:require [clojurewerkz.elastisch.rest          :as esr]
            [clojurewerkz.elastisch.rest.document :as esd]))

(defn connect
  ([] (connect "http://127.0.0.1:9200"))
  ([conn-string] (esr/connect conn-string)))

(defn prepare-response [id doc]
  (assoc doc :id id))

(defn get-doc
  ([conn index type id]
   (let [resp (esd/get conn index type id)]
     (prepare-response id (:_source resp))))
  ([conn type id]
   (get-doc conn "whereabouts_dev" type id))
  ([type id]
   (get-doc (connect) type id)))

(defn set-doc
  ([conn index type id doc]
   (esd/put conn index type id doc)
   (prepare-response id doc))
  ([conn type id doc]
   (set-doc conn "whereabouts_dev" type id doc))
  ([type id doc]
   (set-doc (connect) type id doc)))
