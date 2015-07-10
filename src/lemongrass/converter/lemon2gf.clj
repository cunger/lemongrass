(ns lemongrass.converter.lemon2gf
  (:require [lemongrass.settings :as settings]
            [lemongrass.converter.utils :as utils]
            [lemongrass.converter.signature :as signature]
            [lemongrass.converter.ontology :as ontology]
            [lemongrass.converter.lexicon :as lexicon]
            [lemongrass.converter.render :refer [render]]
            [clojure.tools.logging :as log]
            [seabass.core :as seabass]))


;; MAIN 

(defn run [& files]

  ; why is files of the form ((arg1 arg2 ...)) instead of (arg1 arg2 ...)?
  (let [graph (apply seabass/build (first files))]

    (do
      ; Process ontological and lexical information
      
      (log/info "Processing the ontology...")
  	  (ontology/process graph)
      
      (log/info "Processing the lexicon...")
  	  (lexicon/process  graph)
      
      ; Write abstract syntax and definitions file
      (log/info "Writing abstract syntax...")
      (spit (str settings/target "Domain.gf")  (render "abstract"    @signature/abstract))
      (spit (str settings/target "Domain.clj") (render "definitions" @signature/abstract))

      ; Write a concrete syntax for each language for which a lexicon was provided
      (log/info "Writing concrete syntax...")
  	  (doseq [language (keys @signature/concrete)]
        (let [context (assoc (language @signature/concrete) :language (name language))]
          (spit (str settings/target (str "Domain" (name language) ".gf")) (render "concrete" context))))))

  ;(signature/report)

  (log/info "Domain grammar successfully generated.\n"
            "Grammar:     " (str settings/target "Domain.gf") (clojure.string/join (map #(str ", " settings/target "Domain" % ".gf") (map name (keys @signature/concrete)))) "\n"
            "Definitions: " (str settings/target settings/app ".clj")) 
)
