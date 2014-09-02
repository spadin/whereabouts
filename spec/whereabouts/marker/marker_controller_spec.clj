(ns whereabouts.marker.marker-controller-spec
  (:import (java.io BufferedReader StringReader))
  (:require [speclj.core             :refer :all]
            [cheshire.core           :refer [generate-string]]
            [clojure.java.io         :refer [input-stream]]
            [whereabouts.spec-helper :refer [parse-body delete-index]]
            [whereabouts.marker.marker-controller :refer :all]
            [whereabouts.database.elasticsearch :as elasticsearch]))

(describe "whereabouts.marker.marker-controller"
  (defn generate-request [str]
    (BufferedReader. (StringReader. str)))

  (with location {:lat 1 :lon 1})
  (with id "id")
  (with marker-data {:id @id :location @location})
  (with marker-data-request (generate-request (generate-string @marker-data)))
  (with mock-config {:uri "http://127.0.0.1:9200"
                     :index "whereabouts-test"})

  (around [it]
    (elasticsearch/setup! @mock-config)
    (it))

  (around [it]
    (it)
    (delete-index "whereabouts_dev"))

  (context "/set-marker"
    (it "returns the marker data"
      (should= @marker-data
               (set-marker @marker-data-request)))

    (it "sets the marker data to elastic search"
      (set-marker @marker-data-request)
      (should= @marker-data
               (find-marker @id)))))
