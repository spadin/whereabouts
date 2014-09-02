(ns whereabouts.database.elasticsearch-spec
  (:require [speclj.core                        :refer :all]
            [whereabouts.database.elasticsearch :refer :all]
            [clojurewerkz.elastisch.rest          :as esr]
            [clojurewerkz.elastisch.rest.document :as esd]))

(describe "whereabouts.database.elasticsearch"
  (with mock-connect (fn [uri] uri))
  (with mock-put (fn [conn index type id doc] {:index index}))
  (with mock-config {:uri "http://127.0.0.1:9200"
                     :index "test"})

  (around [it]
    (setup! @mock-config)
    (it))

  (context "/connect"
    (it "returns a db connection"
      (should= clojurewerkz.elastisch.rest.Connection
               (type (connect))))

    (it "calls elasticsearch connect with passed connection string"
      (with-redefs [esr/connect @mock-connect]
        (should= "connection-string"
                 (connect "connection-string"))))

    (it "calls elasticsearch connect with http://127.0.0.1:9200"
      (with-redefs [esr/connect @mock-connect]
        (should= "http://127.0.0.1:9200"
                 (connect)))))

  (context "/set-doc"
    (with-stubs)

    (context "no default arguments"
      (it "returns a hash-map"
        (let [conn (connect)]
          (should= (type {})
                   (type (set-doc conn "whereabouts_dev" "type" "id" {:content 1}))))))

    (context "with default index"
      (it "defaults index to index in config"
        (should-invoke esd/put {:with ["conn" "test" "type" "id" {:content 1}]}
                       (set-doc "conn" "type" "id" {:content 1}))))

    (context "with default connection and index"
      (it "defaults connection to connect function"
        (with-redefs [esd/put @mock-put]
          (should-invoke connect {:times 1}
                         (set-doc "type" "id" {:content 1}))))))

  (context "/get-doc"
    (with-stubs)

    (context "no default arguments"
      (it "returns a hash-map with id and content"
        (let [conn (connect)]
          (set-doc conn "whereabouts_dev" "type" "id" {:content 1})
          (should= {:id "id" :content 1}
                   (get-doc conn "whereabouts_dev" "type" "id")))))

    (context "with default index"
      (it "defaults index to index in config"
        (should-invoke esd/get {:with ["conn" "test" "type" "id"]}
                       (get-doc "conn" "type" "id"))))

    (context "with default connection and index"
      (it "defaults connection to connect function"
        (with-redefs [esd/get (fn [conn index type id] {:index index})]
          (should-invoke connect {:times 1}
                         (get-doc "type" "id"))))))

  (context "/setup!"
    (it "sets the uri"
      (setup! {:uri "test-uri"})
      (should= "test-uri"
               @uri))

    (it "sets the index"
      (setup! {:index "test-index"})
      (should= "test-index"
               @index))))
