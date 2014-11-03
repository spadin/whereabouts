(ns whereabouts.marker.marker-controller-spec
  (:require [speclj.core                          :refer :all]
            [whereabouts.marker.marker-controller :refer :all]
            [whereabouts.spec-helper              :refer :all]
            [ring.mock.request                    :refer [request content-type body]]))

(describe "whereabouts.marker.marker-controller"
  (with id "id")
  (with marker {:location {:lat 5 :lon 5}})
  (with bounding-box {:top_right {:lat 10 :lon 10} :bottom_left {:lat 0 :lon 0}})
  (with expected-response (merge {:id @id} @marker))

  (defn- route [path]
    (str "/" path))

  (defn- post-request [path body]
    (-> (request :post (route path))
        (content-type "application/json")
        (merge {:body body})))

  (defn- get-request [path]
    (request :get (route path)))

  (defn- get-request-with-params [path params]
    (-> (request :get (route path))
        (merge {:params params})))

  (defn- query-string-bounding-box [bounding-box]
    {:top_right_lat   (str (get-in bounding-box [:top_right :lat]))
     :top_right_lon   (str (get-in bounding-box [:top_right :lon]))
     :bottom_left_lat (str (get-in bounding-box [:bottom_left :lat]))
     :bottom_left_lon (str (get-in bounding-box [:bottom_left :lon]))})

  (around [it]
    (elasticsearch-setup)
    (elasticsearch-create-index)
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
    (it "returns an empty list when no markers are found in the area"
      (elasticsearch-flush)
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
                   (:body (marker-handler request))))))

    (context "POST /search-location"
      (it "returns the search results"
        (set-marker @id @marker)
        (elasticsearch-flush)
        (let [request (post-request "search-location" @bounding-box)]
          (should= [@expected-response]
                   (:body (marker-handler request))))))

    (context "GET /search-location"
      (it "returns the search results"
        (set-marker @id @marker)
        (elasticsearch-flush)
        (let [request (get-request-with-params "search-location" (query-string-bounding-box @bounding-box))]
          (should= [@expected-response]
                   (:body (marker-handler request))))))))
