(ns whereabouts.elasticsearch.tasks
  (:require [whereabouts.elasticsearch.core    :refer [create-index delete-index]]
            [whereabouts.config                :refer [set-env!]]))

(defn create-index-on-env [env]
  (set-env! env)
  (delete-index)
  (create-index))

(defn delete-index-on-env [env]
  (set-env! env)
  (delete-index))
