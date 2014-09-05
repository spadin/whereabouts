(ns whereabouts.config)

(defn eval-file [f]
  (read-string (slurp f)))

(defn load-config-map [env]
  (eval-file (str "src/whereabouts/environment/" (name env) "/config.edn")))

(defn run-configs [configs]
  (doseq [[setup-fn config] configs]
    ((resolve setup-fn) config)))

(defn set-env! [env]
  (run-configs (load-config-map env)))
