(ns whereabouts.elasticsearch.tasks
  (:require [clojurewerkz.elastisch.rest.index :as esi]
            [whereabouts.elasticsearch.core    :refer [index connect mappings]]
            [whereabouts.config                :refer [set-env!]]))

(defn create-index-on-env [env]
  (set-env! env)
  (let [index @index
        conn  (connect)]
    (esi/delete conn index)
    (esi/create conn index :mappings @mappings)))

(defn delete-index-on-env [env]
  (set-env! env)
  (esi/delete (connect) @index))
