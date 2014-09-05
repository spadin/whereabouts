(ns whereabouts.database.elasticsearch-setup
  (:require [clojurewerkz.elastisch.rest.index  :as esi]
            [whereabouts.database.elasticsearch :refer [index connect]]
            [whereabouts.config                 :refer [set-env!]]))

(defn create-index-on-env [env]
  (set-env! env)
  (let [index @index
        conn  (connect)]
    (esi/delete conn index)
    (esi/create conn index)))

(defn delete-index-on-env [env]
  (set-env! env)
  (esi/delete (connect) @index))
