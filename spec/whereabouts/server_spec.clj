(ns whereabouts.server-spec
  (:require [speclj.core        :refer :all]
            [whereabouts.server :refer :all]))

(context "/setup!"
  (it "sets the default-port"
    (setup! {:default-port 9999})
    (should= 9999
             @default-port)))

(context "/get-port"
  (around [it]
    (reset! default-port nil)
    (it))

  (it "returns nil when port-or-nil is nil and default-port not set"
    (should= nil
             (get-port nil)))

  (it "returns the port passed in"
    (should= 9999
             (get-port 9999)))

  (it "returns the default-port when port-or-nil is nil"
    (setup! {:default-port 3000})
    (should= 3000
             (get-port nil)))

  (it "returns an int for port when input is a string"
    (should= 9999
             (get-port "9999"))))
