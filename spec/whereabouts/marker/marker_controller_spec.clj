(ns whereabouts.marker.marker-controller-spec
  (:import (java.io BufferedReader StringReader))
  (:require [speclj.core             :refer :all]
            [cheshire.core           :refer [generate-string]]
            [clojure.java.io         :refer [input-stream]]
            [whereabouts.spec-helper :refer [parse-body delete-index]]
            [whereabouts.marker.marker-controller :refer :all]))

(describe "whereabouts.location.location-controller"
  (defn generate-request [str]
    (BufferedReader. (StringReader. str)))

  (with location {:lat 1 :lon 1})
  (with id "id")
  (with marker-data {:id @id :location @location})
  (with marker-data-request (generate-request (generate-string @marker-data)))

  (around [it]
    (it)
    (delete-index "whereabouts_dev"))

  (context "#set-marker"
    (it "returns the marker data"
      (should= @marker-data
               (set-marker @marker-data-request)))

    (it "sets the marker data to elastic search"
      (set-marker @marker-data-request)
      (should= @marker-data
               (find-marker @id)))))
