(defproject whereabouts "0.1.0-SNAPSHOT"
  :description "location service"
  :url "http://github.com/spadin/whereabouts"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[cheshire "5.3.1"]
                 [clojurewerkz/elastisch "2.0.0-rc1"]
                 [compojure "1.1.8"]
                 [http-kit "2.1.16"]
                 [org.clojure/clojure "1.5.1"]
                 [ring/ring-devel "1.3.0"]
                 [ring/ring-jetty-adapter "1.3.0"]]
  :profiles {:dev {:main "whereabouts.environment.development"
                   :dependencies [[speclj "3.0.1"]]}}
  :plugins [[speclj "3.0.1"]
            [lein-ring "0.8.10"]]
  :test-paths ["spec"])
