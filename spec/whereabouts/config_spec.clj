(ns whereabouts.config-spec
  (:require [speclj.core        :refer :all]
            [whereabouts.config :refer :all]))

(describe "whereabouts.config"
  (with-stubs)

  (context "/eval-file"
    (it "calls slurps the file and evals using read-string"
      (with-redefs [read-string (stub :read-string)
                    slurp       (stub :slurp)]
        (eval-file "some/file.edn")
        (should-have-invoked :read-string)
        (should-have-invoked :slurp {:with ["some/file.edn"]}))))

  (context "/load-config-map"
    (it "loads a file based on env argument"
      (with-redefs [eval-file (stub :eval-file)]
        (load-config-map :my-env)
        (should-have-invoked :eval-file {:with ["src/whereabouts/environment/my-env/config.edn"]})))))
