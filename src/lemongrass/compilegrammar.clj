(ns lemongrass.compilegrammar
  (:require [lemongrass.settings   :as settings]
            [clojure.java.io       :as io]
            [clojure.tools.logging :as log]
            [clojure.java.shell    :as shell]
            [clostache.parser      :as clostache]))


(def here "src/lemongrass/")
(def grammar (str here "grammar/"))


;; MAIN 

(declare files-in)
(declare copy-to)
(declare strip-src)
(declare get-languages)


(defn run [temp]

  (let [languages (get-languages)]

    ;; Copy all required grammar modules to temp

    (let [inits [(str grammar "core/Clause/Clause") 
                 (str grammar "core/Utterance/Utterance") 
                 (str grammar "core/Chunk/Chunk")
                 (str grammar "task/" settings/task "/" settings/task)
                 (str settings/target "Domain")]         
          tails  (concat [ ".gf" "I.gf" ] (map #(str % ".gf") languages))
          files  (for [i inits t tails] (str i t))]

         (doseq [f files] (if (.exists (io/file f)) (copy-to f temp))))

    (let [abstract (strip-src (str grammar "abstract.mustache"))
          concrete (strip-src (str grammar "concrete.mustache"))
          context  { :app      settings/app 
                     :domain   "Domain" 
                     :task     settings/task 
                     :startcat settings/startcat }]

         (do
           (spit (str temp settings/app ".gf") 
                 (clostache/render-resource abstract context))
           (doseq [language languages]
           (spit (str temp settings/app language ".gf")
                 (clostache/render-resource concrete (merge context { :language language }))))))

    ;; Compile app grammar in temp

    (let [grammars (clojure.string/join " " (map #(str settings/app % ".gf") languages))
          compiled (shell/sh "gf" "-make" grammars :dir temp)] ; +RTS -K66M -RTS
         (if (not (= (:exit compiled) 0)) 
             (do 
               (log/fatal "Compilation failed with the following error message:" (:err compiled))
               (java.lang.System/exit 1))))

    ;; Generate token list 

    (spit (str temp "tokens.sh")
          (clojure.string/replace (slurp (str here "tokens.sh")) #"GRAMMAR" (str settings/app ".pgf"))) 
    (spit (str temp settings/app ".tok")
          (:out (shell/sh "sh" "tokens.sh" :dir temp)))

    ;; Move compiled grammar, definitions, and tokens into target directory 
    
         (copy-to (str temp settings/app ".pgf") settings/target)
         (copy-to (str temp settings/app ".tok") settings/target)

    ;; Done.

    (log/info "Application grammar successfully compiled.\n"
              "Grammar: " (str settings/target settings/app ".pgf") "\n"
              "Tokens:  " (str settings/target settings/app ".tok"))           
  )
)


;; AUX 

(defn files-in [folder]
  (rest (file-seq (io/file folder))))

(defn copy-to [file folder]
  (let [old-file (io/file file)
        new-file (io/file (str folder (.getName old-file)))]
       (io/copy old-file new-file)))

(defn strip-src [file-name]
  (clojure.string/replace file-name "src/" ""))

(defn get-languages []
  (remove nil?
  (for [f (files-in settings/target)]
       (get (re-matches #"Domain(.+)\.gf" (.getName f)) 1))))