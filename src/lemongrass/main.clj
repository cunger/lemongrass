(ns lemongrass.main
    (:require [lemongrass.settings :as settings]
              [lemongrass.converter.lemon2gf :as lemon2gf]
              [lemongrass.converter.render :as render]
              [lemongrass.converter.signature :as signature]
              [lemongrass.compilegrammar :as compilegrammar]
              [clojure.java.io :as io] 
              [clojure.tools.logging :as log]))


(def temp (str settings/target "temp/"))


;; MAIN 

(defn -main [& files]

  (if (empty? files) 
      (do (log/error "Please provide file paths to the ontology and lexicon(s) as input.")
          (java.lang.System/exit 1)))

  ; Empty target directory
  (doseq [f (remove (partial = (io/file temp))
            (concat (compilegrammar/files-in settings/target)
                    (compilegrammar/files-in temp)))]
         (io/delete-file f true))

  (let [app  (if-not (nil? settings/app)  settings/app  "Application")
        task (if-not (nil? settings/task) settings/task "None")]

    ; Generate domain grammar
    (log/info "Generating domain grammar...")
    (lemon2gf/run files)
    
    ; Compose application grammar
    (log/info "Composing application grammar...")
    (compilegrammar/run temp) 

    ; Store what needs to be accessible in online mode
    ; 1. copy compiled .pgf to gf-server-grammars folder
    (io/copy (io/file (str settings/target app ".pgf")) (io/file (str settings/gf-server-grammars app ".pgf")))
    ; 2. store definitions in semantic-definitions folder
    (spit (str settings/target "Domain.clj") (render/render "definitions" @signature/abstract))
    ; 3. store tokens file in resources
    (io/copy (io/file (str temp "tokens")) (io/file (str settings/target "tokens")))

    (log/info "\nDone.")
    (java.lang.System/exit 0)))
