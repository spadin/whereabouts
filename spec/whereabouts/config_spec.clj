(ns whereabouts.config-spec
  (:require [speclj.core        :refer :all]
            [whereabouts.config :refer :all]
            [clojure.java.io    :refer [resource]]))

(describe "whereabouts.config"
  (with-stubs)

  (context "/eval-file"
    (it "calls slurps the file and evals using read-string"
      (with-redefs [read-string (stub :read-string)
                    slurp       (stub :slurp)
                    resource    (stub :resource)]
        (eval-file "some/file.edn")
        (should-have-invoked :read-string)
        (should-have-invoked :slurp)
        (should-have-invoked :resource {:with ["some/file.edn"]}))))

  (context "/load-config-map"
    (it "loads a file based on env argument"
      (with-redefs [eval-file (stub :eval-file)]
        (load-config-map :my-env)
        (should-have-invoked :eval-file {:with ["whereabouts/environment/my-env/config.edn"]})))))
