(ns whereabouts.database.elasticsearch-setup-spec
  (:require [speclj.core                              :refer :all]
            [whereabouts.config                       :refer [set-env!]]
            [whereabouts.database.elasticsearch       :refer :all]
            [whereabouts.database.elasticsearch-setup :refer :all]
            [clojurewerkz.elastisch.rest          :as esr]
            [clojurewerkz.elastisch.rest.document :as esd]
            [clojurewerkz.elastisch.rest.index    :as esi]))

(describe "whereabouts.database.elasticsearch-setup"
  (with mock-config {:uri "http://127.0.0.1:9200"
                     :index "whereabouts-test"})

  (around [it]
    (with-redefs [set-env! (constantly nil)]
      (let [connection (connect)]
        (setup! @mock-config)
        (esi/delete connection @index)
        (it))))

  (context "indexes"
    (context "/create-index-on-env"
      (it "creates the elasticsearch index"
        (create-index-on-env :test)
        (should= true
                 (esi/exists? (connect) "whereabouts-test")))

      (it "should not throw an error if the index exists"
        (create-index-on-env :test)
        (should-not-throw
          (create-index-on-env :test))))

    (context "/delete-index-on-env"
      (it "deletes the elasticsearch index"
        (create-index-on-env :test)
        (delete-index-on-env :test)
        (should= false
                 (esi/exists? (connect) "whereabouts-test"))))))
