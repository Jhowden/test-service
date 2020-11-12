(defproject test-service "0.0.1-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [io.pedestal/pedestal.service "0.5.8"]

                 ;; Remove this line and uncomment one of the next lines to
                 ;; use Immutant or Tomcat instead of Jetty:
                 [io.pedestal/pedestal.jetty "0.5.8"]
                 ;; [io.pedestal/pedestal.immutant "0.5.8"]
                 ;; [io.pedestal/pedestal.tomcat "0.5.8"]
                ;  [hiccup "1.0.5"]
                 [reagent "0.10.0"]
                 [clj-commons/secretary "1.2.4"]
                 [figwheel-sidecar "0.5.18"]
                 [com.bhauman/rebel-readline "0.1.4"]

                 [ch.qos.logback/logback-classic "1.2.3" :exclusions [org.slf4j/slf4j-api]]
                 [org.slf4j/jul-to-slf4j "1.7.26"]
                 [org.slf4j/jcl-over-slf4j "1.7.26"]
                 [org.slf4j/log4j-over-slf4j "1.7.26"]]
  :min-lein-version "2.0.0"
  :resource-paths ["config", "resources"]
  :source-paths ["src/clj" "src/cljc"]
  ;; If you use HTTP/2 or ALPN, use the java-agent to pull in the correct alpn-boot dependency
  ;:java-agents [[org.mortbay.jetty.alpn/jetty-alpn-agent "2.0.5"]]
  :profiles {
             :dev {
                   :aliases {
                             "run-dev" ["trampoline" "run" "-m" "test-service.server/run-dev"]
                             "dev" ["trampoline" "run" "-m" "clojure.main" "--init" "script/figwheel.clj" "-m" "rebel-readline.main"]
                             }
                   :dependencies [[io.pedestal/pedestal.service-tools "0.5.8"]]
                  }
             :uberjar {
                       :aot [test-service.server]
                      }
            }
  :plugins [[lein-figwheel "0.5.18"]]
  :clean-targets ^{:protect false} [:target-path "resources/public/js/out" "resources/public/js/test_service.js"]
  :cljsbuild {
    :builds [{
              :id "dev"
              :source-paths ["src/cljc" "src/cljs"]
              :figwheel true
              ;; :figwheel     {:on-jsload "test-service.core"}
              :compiler {
                         :main test-service.core
                         :asset-path "js/out"
                         :output-to "resources/public/js/test_service.js"
                         :output-dir "resources/public/js/out"
                         :source-map-timestamp true
                        }
             }]
   }
  :figwheel {
             :css-dirs ["resources/public/css"]
            }
  :main ^{:skip-aot true} test-service.server)
