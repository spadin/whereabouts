(defproject whereabouts "0.1.0-SNAPSHOT"
  :description "location service with simple api"
  :url "http://github.com/spadin/whereabouts"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[clojurewerkz/elastisch "2.1.0-beta5"]
                 [compojure "1.1.8"]
                 [org.clojure/clojure "1.5.1"]
                 [org.clojure/tools.nrepl "0.2.3"]
                 [ring/ring-jetty-adapter "1.3.0"]
                 [ring/ring-json "0.3.1"]]
  :min-lein-version "2.0.0"
  :profiles {:dev  {:main whereabouts.environment.development.run
                    :dependencies [[ring/ring-devel "1.3.0"]
                                   [ring-mock "0.1.5"]
                                   [speclj "3.0.1"]]
                    :aliases {"create-index" ["do" ["create-index-on-env" :development]]
                              "delete-index" ["do" ["delete-index-on-env" :development]]}}
             :prod {:main whereabouts.environment.production.run
                    :aliases {"create-index" ["do" ["create-index-on-env" :production]]
                              "delete-index" ["do" ["delete-index-on-env" :production]]}}
             :test {:aliases {"create-index" ["do" ["create-index-on-env" :test]]
                              "delete-index" ["do" ["delete-index-on-env" :test]]}}}
  :aliases {"create-index-on-env" ["run" "-m" "whereabouts.database.elasticsearch-setup/create-index-on-env"]
            "delete-index-on-env" ["run" "-m" "whereabouts.database.elasticsearch-setup/delete-index-on-env"]}
  :plugins [[speclj "3.0.1"]
            [lein-ring "0.8.10"]]
  :test-paths ["spec"])
