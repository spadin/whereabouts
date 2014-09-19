(ns whereabouts.elasticsearch.core
  (:require [clojurewerkz.elastisch.query         :as q]
            [clojurewerkz.elastisch.rest          :as es]
            [clojurewerkz.elastisch.rest.document :as esd]
            [clojurewerkz.elastisch.rest.index    :as esi]
            [clojurewerkz.elastisch.rest.response :as esr]))

(def uri        (atom nil))
(def index      (atom nil))
(def mappings   (atom nil))
(def connection (atom nil))

(defn connect []
  (es/connect @uri))

(defn setup! [config]
  (reset! uri        (:uri      config))
  (reset! index      (:index    config))
  (reset! mappings   (:mappings config))
  (reset! connection (connect)))

(defn prepare-response [id doc]
  (assoc doc :id id))

(defn- source [resp]
  (:_source resp))

(defn create-index []
  (esi/delete @connection @index)
  (esi/create @connection @index :mappings @mappings))

(defn delete-index []
  (esi/delete @connection @index))

(defn get-doc [type id]
   (let [resp (esd/get @connection @index type id)]
     (prepare-response id (source resp))))

(defn put-doc [type id doc]
  (esd/put @connection @index type id doc)
  (prepare-response id doc))

(defn- search-location-response [type bounding-box]
  (esd/search @connection @index type
              :query (q/filtered :query (q/match-all)
                                 :filter {:geo_bounding_box {"location" bounding-box}})))

(defn search-location [type bounding-box]
  (let [resp (search-location-response type bounding-box)
        hits (esr/hits-from resp)
        ids  (map #(:_id %) hits)]
    (map #(prepare-response %1 (source %2)) ids hits)))
