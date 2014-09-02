(ns whereabouts.server)

(def default-port (atom nil))

(defn setup! [config]
  (reset! default-port (:default-port config)))
