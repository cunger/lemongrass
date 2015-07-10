(ns lemongrass.converter.signature
  (:require [lemongrass.settings :as settings]
            [clojure.tools.logging :as log]))


(def iso "src/lemongrass/converter/config/iso639")
(def lin "src/lemongrass/converter/config/lincats")
(def log (str settings/target "report.log"))


;; DEF

(def namespaces   (atom {}))
(def namespaces-i (atom 0))

(def sense-index  (atom {}))

(def abstract     (atom {}))
(def concrete     (atom {}))


;; COUNT

(def  i 0)
(defn fresh! [] (do (set! i (+ i 1)) i))


;; NAMESPACES

(defn add-to-namespaces! [k v]
  (swap! namespaces assoc k v)
  (swap! namespaces-i + @namespaces-i 1))

;; LANGUAGE 

(def language-map (atom {}))

(defn init-language-map! []
  (with-open [rdr   (clojure.java.io/reader iso)]
      (doseq [line  (line-seq rdr)]
        (let [codes (clojure.string/split line #":")]
         (if (not   (clojure.string/blank? (nth codes 4)))
             (doseq [code (filter (comp not clojure.string/blank?) codes)]
              (swap! language-map assoc (clojure.string/trim code) (clojure.string/trim (nth codes 4)))))))))

;; LINCATS 

(def lincat-map (atom {}))

(defn init-lincat-map! []
  (with-open [rdr   (clojure.java.io/reader lin)]
      (doseq [line  (line-seq rdr)]
         (if-not (clojure.string/blank? (clojure.string/trim line))
             (let [parts (clojure.string/split line #":")]
                  (swap! lincat-map assoc (clojure.string/trim (get parts 0)) (clojure.string/trim (get parts 1))))))))

;; ABSTRACT

(defn init-abstract! []
  (swap! abstract assoc :concepts []))

(defn add-to-abstract! [uri sense] 
  (if (contains? @sense-index uri)
      (if-not (= (get @sense-index uri) sense) 
              (log/warn "Trying to overwrite a sense in abstract:\n* Old:" (get @sense-index uri) "\n* New:" sense))
      ; add all possible identifiers 
      (let [lincats {"Entity"    ["NP"]
                     "Predicate" ["CN" "VP" "AP" "Adv"]
                     "Relation"  ["V2" "N2" "A2" "Prep"]
                     "Statement" ["Cl"]}
            senses  (for [l (get lincats (:cat sense))]
                         (assoc (assoc sense :identifier (str (:identifier sense) "_" l)) :type (str (:cat sense) "_" l)))]
           (swap! abstract assoc :concepts (concat senses (:concepts @abstract))) 
           (swap! sense-index assoc uri sense)
           sense)))

(defn clean-abstract! []
  """Removes all concepts that don't have a linearization"""
  (let [linearized (for [language (keys @concrete)
                         lincat   (:lincat (language @concrete))]
                        (clojure.string/trim (get (clojure.string/split lincat #"=") 0)))]
       (doseq [k (keys @abstract)
               v (k @abstract)]
              (if (not (some #{(:identifier v)} linearized)) 
                  (swap! abstract assoc k (remove #{v} (k @abstract)))))))

;; CONCRETE

; INIT 

(defn init-concrete! []
  (reset! concrete {}))

(defn add-language-to-concrete! [language]
  (swap! concrete assoc language { :lincat nil :lin nil :oper nil }))

; UPDATE

(declare update-concrete!)

(defn add-lin-to-concrete! [language element]
  (let [elements (:lin    (language @concrete))
        lincats  (:lincat (language @concrete))
        id       (str (:identifier element) "_" (:lincat element))]
    ; add lincat 
    (update-concrete! language :lincat (assoc lincats id (:lincat element)))
    ; add lin 
    (if (contains? elements id) 
        (update-concrete! language :lin (assoc elements id { :lin (cons (:lin element) (:lin (get elements id))) :args (:args element) })) ; TODO what if args clash
        (update-concrete! language :lin (assoc elements id { :lin (cons (:lin element) '()) :args (:args element) })))))

(defn add-oper-to-concrete! [language element]
  (let [elements (:oper (language @concrete))]
    (if-not (some #{element} elements)
            (update-concrete! language :oper (cons element elements)))))

(defn update-concrete! [language k new-elements]
  (swap! concrete assoc language (assoc (language @concrete) k new-elements)))

; SERIALIZE

(defn serialize-lins! []
  (doseq [language (keys @concrete)]
    (update-concrete! language :lincat
      (for [[k v] (:lincat (language @concrete))]
                  (str k " = " v ";")))
    (update-concrete! language :lin 
      (for [[k v] (:lin (language @concrete))] 
                  (str k " " (clojure.string/join " " (:args v)) " = " (clojure.string/join " | " (distinct (:lin v))) ";")))))


;; REPORTING

(defn report [] 
  (with-open [w (clojure.java.io/writer log)]
    (log/info "Writing log to:" log)
    (.write w (str "lemongrass log (" (java.util.Date.) ")"))
    (.write w "\n------ @abstract ------")
    (.write w "\nConcepts:")
    (doseq [c (:concepts @abstract)]
      (.write w (str "\n* " (:identifier c) " :" (:cat c) " >> " (:definition c))))
  ; (println "------ @sense-index ---")
  ; (doseq [ [k v] @signature/sense-index ]
  ;   (println (str k " >> " v)))
  ; (println "-----------------------")
    (.write w "\n------ @namespaces ---")
    (doseq [ [k v] @namespaces ]
      (.write w (str "\n" k " >> " v)))
    (.write w "\n------ @concrete -------")
    (.write w "\nLin:")
    (doseq [[k v] (:lin @concrete)]
      (.write w (str "\n* " k " " (:args v) " = " (:lin v))))
    (.write w "\nOper:")
    (doseq [[k v] (:oper @concrete)]
     (.write w (str "\n* " k " = " v)))
))