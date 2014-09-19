(ns whereabouts.spec-helper
  (:require [whereabouts.elasticsearch.core       :as elasticsearch]
            [whereabouts.elasticsearch.elasticsearch-setup :as elasticsearch-setup]
            [clojurewerkz.elastisch.rest.index        :as esi]))

(defmacro with-elasticsearch [bindings & body]
  `(let [~(first bindings)  (elasticsearch/connect)
         ~(second bindings) @elasticsearch/index]
     ~@body))

(defn elasticsearch-flush []
  (with-elasticsearch [connection index]
    (esi/flush connection index)))

(defn elasticsearch-setup []
  (elasticsearch/setup! {:uri   "http://127.0.0.1:9200"
                         :index "whereabouts-test"}))

(defn elasticsearch-create-index []
  (elasticsearch-setup/create-index-on-env :test))

(defn elasticsearch-delete-index []
  (elasticsearch-setup/delete-index-on-env :test))

(defn elasticsearch-index-exists? []
  (with-elasticsearch [connection index]
    (esi/exists? connection index)))
