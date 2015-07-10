(ns lemongrass.settings)

;; Converter 

(def task "None")        ; Task ; Querying 
(def app  "Application") ; Name of the final application

(def target "src/lemongrass/target/") ; Target directory for all auto-generated files

;; GF 

(def gf-server-grammars (str (java.lang.System/getProperty "user.home") "/.cabal/share/gf-3.6.10/www/grammars/"))
(def localhost "http://localhost:41296/grammars/")

(def startcat "Utterance_Utt") 