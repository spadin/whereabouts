(ns whereabouts.spec-helper
  (:require [cheshire.core :refer [parse-string]]
            [whereabouts.database.elasticsearch :as es]
            [clojurewerkz.elastisch.rest.index  :as esi]))

(defn parse-body [response]
  (parse-string (:body response) true))
