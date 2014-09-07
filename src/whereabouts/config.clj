(ns whereabouts.config
  (:require [clojure.string :as string]))

(defn eval-file [f]
  (read-string (slurp f)))

(defn load-config-map [env]
  (eval-file (str "src/whereabouts/environment/" (name env) "/config.edn")))

(defn run-configs [configs]
  (doseq [[setup config] configs]
    (let [[setup-ns setup-fn-name] (map #(symbol %) (string/split (str setup) #"\/"))]
      (require setup-ns)
      ((ns-resolve setup-ns setup-fn-name) config))))

(defn set-env! [env]
  (run-configs (load-config-map env)))
