(ns whereabouts.server-spec
  (:require [speclj.core        :refer :all]
            [whereabouts.server :refer :all]))

(context "/setup!"
  (it "sets the default-port"
    (setup! {:default-port 9999})
    (should= 9999
             @default-port)))
