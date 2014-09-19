(ns whereabouts.elasticsearch.core-spec
  (:require [speclj.core                          :refer :all]
            [whereabouts.spec-helper              :refer :all]
            [whereabouts.elasticsearch.core       :refer :all]
            [clojurewerkz.elastisch.rest          :as esr]
            [clojurewerkz.elastisch.rest.document :as esd]
            [clojurewerkz.elastisch.rest.index    :as esi]))

(describe "whereabouts.elasticsearch.elasticsearch"
  (with mock-connect (fn [uri] uri))
  (with mock-put (fn [conn index type id doc] {:index index}))

  (around [it]
    (elasticsearch-setup)
    (it))

  (context "/connect"
    (it "returns a db connection"
      (should= clojurewerkz.elastisch.rest.Connection
               (type (connect))))

    (it "calls elasticsearch connect with http://127.0.0.1:9200"
      (with-redefs [esr/connect @mock-connect]
        (should= "http://127.0.0.1:9200"
                 (connect)))))

  (context "/put-doc"
    (it "returns a hash-map"
      (should= (type {})
               (type (put-doc "type" "id" {:content 1}))))

    (it "defaults index to index in config"
      (should-invoke esd/put {:with [:* "whereabouts-test" "type" "id" {:content 1}]}
                     (put-doc "type" "id" {:content 1})))

    (it "defaults connection to connect function"
      (with-redefs [esd/put @mock-put]
        (should-invoke connect {:times 1}
                       (put-doc "type" "id" {:content 1})))))

  (context "/get-doc"
    (context "no default arguments"
      (it "returns a hash-map with id and content"
        (put-doc "type" "id" {:content 1})
        (should= {:id "id" :content 1}
                 (get-doc "type" "id"))))

    (context "with default index"
      (it "defaults index to index in config"
        (should-invoke esd/get {:with [:* "whereabouts-test" "type" "id"]}
                       (get-doc "type" "id"))))

    (context "with default connection and index"
      (it "defaults connection to connect function"
        (with-redefs [esd/get (fn [conn index type id] {:index index})]
          (should-invoke connect {:times 1}
                         (get-doc "type" "id"))))))

  (context "/search-location"
    (with id "id")
    (with marker {:location {:lat 5 :lon 5}})
    (with expected-response (merge {:id @id} @marker))
    (with bounding-box {:top_left {:lat 10 :lon 0} :bottom_right {:lat 0 :lon 10}})

    (around [it]
      (elasticsearch-create-index)
      (it))

    (it "returns an empty list when nothing found"
      (should-be empty? (search-location "marker" @bounding-box)))

    (it "returns list with match when found"
      (put-doc "marker" @id @marker)
      (elasticsearch-flush)
      (should= [@expected-response]
               (search-location "marker" @bounding-box))))

  (context "/setup!"
    (it "sets the uri"
      (setup! {:uri "test-uri"})
      (should= "test-uri"
               @uri))

    (it "sets the index"
      (setup! {:index "test-index"})
      (should= "test-index"
               @index))

    (it "sets the mappings"
      (setup! {:mappings "test-mappings"})
      (should= "test-mappings"
               @mappings))))
