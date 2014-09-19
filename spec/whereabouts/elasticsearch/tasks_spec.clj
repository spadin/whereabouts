(ns whereabouts.elasticsearch.tasks-spec
  (:require [speclj.core                     :refer :all]
            [whereabouts.spec-helper         :refer :all]
            [whereabouts.elasticsearch.tasks :refer :all]
            [whereabouts.config              :refer [set-env!]]))

(describe "whereabouts.elasticsearch.elasticsearch-setup"
  (around [it]
    (with-redefs [set-env! (constantly nil)]
      (elasticsearch-setup)
      (with-elasticsearch [connection index]
        (elasticsearch-delete-index)
        (it))))

  (context "indexes"
    (context "/create-index-on-env"
      (it "creates the elasticsearch index"
        (create-index-on-env :test)
        (should (elasticsearch-index-exists?)))

      (it "should not throw an error if the index exists"
        (create-index-on-env :test)
        (should-not-throw
          (create-index-on-env :test))))

    (context "/delete-index-on-env"
      (it "deletes the elasticsearch index"
        (create-index-on-env :test)
        (delete-index-on-env :test)
        (should-not (elasticsearch-index-exists?))))))
