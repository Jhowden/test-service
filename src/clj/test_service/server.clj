(ns test-service.server
  (:gen-class) ; for -main method in uberjar
  (:require [io.pedestal.http :as server]
            [io.pedestal.http.route :as route]
            [io.pedestal.http.route.definition.table :refer [table-routes]]
            [test-service.service :as service]))

;; This is an adapted service map, that can be started and stopped
;; From the REPL you can call server/start and server/stop on this service
(defonce runnable-service (server/create-server service/service))

(defn print-routes
  "Print our application's routes"
  []
  (route/print-routes (table-routes service/routes)))

(defn named-route
  "Finds a route by name"
  [route-name]
  (->> service/routes
        table-routes
        (filter #(= route-name (:route-name %)))
        first))

(defn print-route
  "Prints a route and its interceptors"
  [rname]
  (letfn [(joined-by
            [s coll]
            (apply str (interpose s coll)))

          (repeat-str
            [s n]
            (apply str (repeat n s)))

          (interceptor-info
            [i]
            (let [iname  (or (:name i) "<handler>")
                  stages (joined-by
                          ","
                          (keys
                            (filter
                              (comp (complement nil?) val)
                              (dissoc i :name))))]
              (str iname " (" stages ")")))]
    (when-let [rte (named-route rname)]
      (let [{:keys [path method route-name interceptors]} rte
            name-line (str "[" method " " path " " route-name "]")]
        (joined-by
          "\n"
          (into [name-line (repeat-str "-" (count name-line))]
                (map interceptor-info interceptors)))))))

;; we can reload changes with by running (require :reload `test-service.server) in the repl
(defn run-dev
  "The entry-point for 'lein run-dev'"
  [& args]
  (println "\nCreating your [DEV] server...")
  (-> service/service ;; start with production configuration
      (merge {:env :dev
              ;; do not block thread that starts web server
              ::server/join? false
              ;; Routes can be a function that resolve routes,
              ;;  we can use this to set the routes to be reloadable
              ::server/routes #(route/expand-routes (deref #'service/routes))
              ;; all origins are allowed in dev mode
              ::server/allowed-origins {:creds true :allowed-origins (constantly true)}
              ;; Content Security Policy (CSP) is mostly turned off in dev mode
              ::server/secure-headers {:content-security-policy-settings {:object-src "'none'"}}})
      ;; Wire up interceptor chains
      server/default-interceptors
      server/dev-interceptors
      server/create-server
      server/start))

(defn stop-dev
  [server]
  (server/stop server))

(defn -main
  "The entry-point for 'lein run'"
  [& args]
  (println "\nCreating your server...")
  (server/start runnable-service))

;; If you package the service up as a WAR,
;; some form of the following function sections is required (for io.pedestal.servlet.ClojureVarServlet).

;;(defonce servlet  (atom nil))
;;
;;(defn servlet-init
;;  [_ config]
;;  ;; Initialize your app here.
;;  (reset! servlet  (server/servlet-init service/service nil)))
;;
;;(defn servlet-service
;;  [_ request response]
;;  (server/servlet-service @servlet request response))
;;
;;(defn servlet-destroy
;;  [_]
;;  (server/servlet-destroy @servlet)
;;  (reset! servlet nil))

