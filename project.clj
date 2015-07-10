(defproject lemongrass "0.1.0-SNAPSHOT"
  :description "If life gives you lemons, make grammars!"
  :url ""
  :license {:name "GNU General Public License"
            :url "http://www.gnu.org/licenses/gpl.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/tools.logging "0.3.1"]
                 [org.clojure/math.combinatorics "0.0.8"]
                 [org.clojure/data.json "0.2.5"]
                 [de.ubercode.clostache/clostache "1.4.0"]
                 [http-kit "2.1.16"]
                 [seabass "2.1.1"]]
  :main lemongrass.main
  :target-path "target/%s"
  :test-path "test/%s"
  :profiles {:uberjar {:aot :all}})
