(ns whereabouts.elasticsearch.elasticsearch
  (:require [clojurewerkz.elastisch.query         :as q]
            [clojurewerkz.elastisch.rest          :as es]
            [clojurewerkz.elastisch.rest.document :as esd]
            [clojurewerkz.elastisch.rest.index    :as esi]
            [clojurewerkz.elastisch.rest.response :as esr]))

(def uri      (atom nil))
(def index    (atom nil))
(def mappings (atom nil))

(defn setup! [config]
  (reset! uri      (:uri      config))
  (reset! index    (:index    config))
  (reset! mappings (:mappings config)))

(defn connect
  ([]    (connect @uri))
  ([uri] (es/connect uri)))

(defn prepare-response [id doc]
  (assoc doc :id id))

(defn get-doc [type id]
   (let [resp (esd/get (connect) @index type id)]
     (prepare-response id (:_source resp))))

(defn set-doc [type id doc]
  (esd/put (connect) @index type id doc)
  (prepare-response id doc))

(defn search-location [type bounding-box]
  (let [resp (esd/search (connect) @index type :query (q/filtered :query (q/match-all)
                                                                  :filter {:geo_bounding_box {"location" bounding-box}}))
        hits (esr/hits-from resp)
        ids  (map #(:_id %) hits)]
    (map #(prepare-response %1 (:_source %2)) ids hits)))
