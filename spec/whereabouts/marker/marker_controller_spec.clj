(ns whereabouts.marker.marker-controller-spec
  (:require [speclj.core                          :refer :all]
            [whereabouts.marker.marker-controller :refer :all]
            [whereabouts.spec-helper              :refer :all]
            [ring.mock.request                    :refer [request content-type]]))

(describe "whereabouts.marker.marker-controller"
  (with id "id")
  (with marker {:location {:lat 5 :lon 5}})
  (with expected-response (merge {:id @id} @marker))

  (defn- route [path]
    (str "/" path))

  (defn- post-request [path body]
    (-> (request :post (route path))
        (content-type "application/json")
        (merge {:body body})))

  (defn- get-request [path]
    (request :get (route path)))

  (around [it]
    (elasticsearch-setup)
    (it))

  (context "/set-marker"
    (it "returns the marker data with id"
      (should= @expected-response
               (set-marker @id @marker))))

  (context "/get-marker"
    (it "returns the marker from elasticsearch"
      (set-marker @id @marker)
      (should= @expected-response
               (get-marker @id))))

  (context "/search"
    (with bounding-box {:top_left {:lat 10 :lon 0} :bottom_right {:lat 0 :lon 10}})

    (around [it]
      (elasticsearch-create-index)
      (it))

    (it "returns an empty list when no markers are found in the area"
      (should-be empty? (search @bounding-box)))

    (it "returns a list with the marker"
      (set-marker @id @marker)
      (elasticsearch-flush)
      (should= [@expected-response]
               (search @bounding-box))))

  (context "routes"
    (context "GET /:id"
      (it "returns the marker with id"
        (set-marker @id @marker)
        (should= @expected-response
                 (:body (marker-handler (get-request @id))))))

    (context "POST /:id"
      (it "returns the marker with id"
        (let [request (post-request @id @marker)]
          (should= @expected-response
                   (:body (marker-handler request))))))))
