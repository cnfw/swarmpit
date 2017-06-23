(ns swarmpit.router
  (:require [bidi.router :as br]
            [swarmpit.routes :as routes]
            [swarmpit.storage :as storage]
            [swarmpit.controller :as controller]))

(defonce location (atom nil))

(defn- on-navigate
  [location]
  (if (nil? (storage/get "token"))
    (controller/dispatch {:handler :login})
    (controller/dispatch location)))

(defn start
  []
  (let [router (br/start-router! routes/frontend {:on-navigate on-navigate})
        route (:handler @location)]
    (if (some? route)
      (br/set-location! router @location))))


