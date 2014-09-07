(ns whereabouts.marker.marker-controller-spec
  (:require [speclj.core                          :refer :all]
            [whereabouts.marker.marker-controller :refer :all]
            [ring.mock.request                    :refer [request content-type]]
            [whereabouts.database.elasticsearch :as elasticsearch]))

(describe "whereabouts.marker.marker-controller"
  (with id "id")
  (with marker {:location {:lat 1 :lon 1}})
  (with expected-response (merge {:id @id} @marker))
  (with mock-config {:uri "http://127.0.0.1:9200"
                     :index "whereabouts-test"})

  (defn- route [path]
    (str "/" path))

  (defn- post-request [path body]
    (-> (request :post path)
        (content-type "application/json")
        (merge {:body body})))

  (defn- get-request [path]
    (request :get path))

  (around [it]
    (elasticsearch/setup! @mock-config)
    (it))

  (context "/set-marker"
    (it "returns the marker data with id"
      (should= @expected-response
               (set-marker @id @marker)))

    (it "sets the marker data to elasticsearch"
      (set-marker @id @marker)
      (should= @expected-response
               (elasticsearch/get-doc :marker @id))))

  (context "/get-marker"
    (it "returns the marker from elasticsearch"
      (set-marker @id @marker)
      (should= @expected-response
               (find-marker @id))))

  (context "routes"
    (context "GET /:id"
      (it "returns the marker with id"
        (set-marker @id @marker)
        (should= @expected-response
                 (:body (marker-handler (get-request (route @id)))))))

    (context "POST /:id"
      (it "returns the marker with id"
        (let [request (post-request (route @id) @marker)]
          (should= @expected-response
                   (:body (marker-handler request))))))))
