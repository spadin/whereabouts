(ns whereabouts.database.elasticsearch
  (:require [clojurewerkz.elastisch.rest          :as esr]
            [clojurewerkz.elastisch.rest.document :as esd]
            [clojurewerkz.elastisch.rest.index    :as esi]))

(def uri      (atom nil))
(def index    (atom nil))
(def mappings (atom nil))

(defn setup! [config]
  (reset! uri      (:uri      config))
  (reset! index    (:index    config))
  (reset! mappings (:mappings config)))

(defn connect
  ([]    (connect @uri))
  ([uri] (esr/connect uri)))

(defn prepare-response [id doc]
  (assoc doc :id id))

(defn get-doc [type id]
   (let [resp (esd/get (connect) @index type id)]
     (prepare-response id (:_source resp))))

(defn set-doc [type id doc]
  (esd/put (connect) @index type id doc)
  (prepare-response id doc))
