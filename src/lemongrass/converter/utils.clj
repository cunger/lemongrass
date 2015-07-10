(ns lemongrass.converter.utils
  (:require [lemongrass.converter.signature :as signature]
            [lemongrass.converter.utils :refer :all]
            [clojure.set :as set]))


;; RDF

(def rdf-type "(Term. :uri \"http://www.w3.org/1999/02/22-rdf-syntax-ns#type\")")


;; STRING 

(defn contains-string? [string substring]
  (not (nil? (re-matches (re-pattern (str ".*" substring ".*")) string))))

(defn wrap [operator strings]
  (let [len (count strings)]
    (clojure.string/join (concat (for [i (range 0 len)] 
                                      (if (= i (- len 1)) 
                                          (nth strings i)
                                          (str "(" operator " " (nth strings i) " "))) 
                                 (for [i (range 1 len)] ")")))))


;; SEQS

(defn init [ls] (take (- (count ls) 1) ls))

(defn minus    [ls1 ls2] (vec (set/difference   (set ls1) (set ls2))))
(defn overlap  [ls1 ls2] (vec (set/intersection (set ls1) (set ls2))))
(defn overlap? [ls1 ls2] (not (empty? (overlap ls1 ls2))))

(defn elem [e ls] (some #{e} ls))


;; ARRAY

(defn fmap-vals [f m]
  (into {} (for [[k v] m] [k (f v)])))

(defn fmap-keys [f m]
  (into {} (for [[k v] m] [(f k) v])))

(defn remove-nil-values [m] 
  (apply dissoc m (filter (fn [k] (nil? (k m))) (keys m))))

(defn clean-map [m]
  ; remove nil and [] values, and [v] --> v
  (fmap-vals (fn [v] (if (and (sequential? v) (= (count v) 1)) (first v) v)) 
             (apply dissoc (remove-nil-values m) (filter (fn [k] (and (sequential? (get m k)) (empty? (get m k)))) (keys m)))))

(defn conjoin-values [x y] 
  (let [conjoined (distinct 
                  (let [x-a-coll (coll? x)
                        y-a-coll (coll? y)]
                  (cond (and x-a-coll (not y-a-coll)) (cons y x)
                        (and y-a-coll (not x-a-coll)) (cons x y)
                        (and x-a-coll y-a-coll) (concat x y)
                        (and (not x-a-coll) (not y-a-coll)) [x y])))]
        (if (= (count conjoined) 1) (first conjoined) conjoined)))

(defn apply-to-conjoined [f x]
  (if (sequential? x) (map f x) (f x))) 

(defn find-equivalence-classes [xs bool-func]
  ; returns a list of list of x's (where each list is an equivalence class, i.e. (func x1 x2) for any two elements is true)
  (reduce (fn [equivalence-classes x] 
            (loop [done []
                   todo equivalence-classes]
              (if (empty? todo)
                  (cons [x] done) ; case where no equivalence class for x was found
                  (let  [c (first todo)]
                        (if (bool-func x (first c))
                            (concat (cons (cons x c) done) (rest todo)) ; case where c is an equivalence for x
                            (recur (cons c done) (rest todo)))))))      ; case where c is not an equivalence class for x
          []
          xs))

(defn differ-only-in [array1 array2 ks]
  (every? (fn [k] (or (= (get array1 k) (get array2 k)) 
                      (elem k ks)))
          (concat (keys array1) (keys array2))))

(defn collapse [arrays]
  (apply (partial merge-with conjoin-values) arrays))

(defn collapse-bindings [arrays keys-to-collapse]
  ; collapses all arrays that differ at most in values for keys-to-collapse
  (map collapse (find-equivalence-classes arrays (fn [a1 a2] (differ-only-in a1 a2 keys-to-collapse)))))


;; URI 

(defn uri? [string] (or (contains-string? string "/") (contains-string? string "#")))

;; URI TO IDENTIFIER

(defn to-ascii [string]
  (clojure.string/replace string #"[^\x00-\x7F]" ""))

(defn to-identifier [string]
  ((comp 
    #(clojure.string/replace % #"\s+" "_")
    #(clojure.string/replace % #"-"   "_")
    #(clojure.string/replace % #"\+"  "_")
    #(clojure.string/replace % #"/"   "_")
    #(to-ascii %))
   string))

(defn frag [uri]
  ; if uri contains neither "#" nor "/"
  (if (and (not (contains-string? uri "#")) (not (contains-string? uri "/")))
      ; probably: if it contains ":", it is a blank node, else a literal
      (str (if (contains-string? uri ":") "node_" "literal_") (clojure.string/replace uri #"[^A-Za-z0-9]" ""))
      ; else proceed
      (let [matching-prefixes (filter #(.startsWith uri %) (keys @signature/namespaces))]
        (if-not (empty? matching-prefixes)
          ; if prefix of uri is in signature/namespaces, replace it accordingly
          (let [prefix (first matching-prefixes)
                ident  (clojure.string/replace uri prefix "")]
            (str (get @signature/namespaces prefix) "_" (to-identifier ident)))
          ; otherwise replace prefix with new abbreviation and store it in signature/namespaces 
          (let [new-n       (str "n" @signature/namespaces-i)
                hash-split  (clojure.string/split uri #"#")
                slash-split (clojure.string/split uri #"/")
                split       (if (> (count hash-split) 1) hash-split slash-split)
                separator   (if (> (count hash-split) 1) "#" "/")
                prefix      (str (clojure.string/join separator (butlast split)) separator)]
            (do 
               (signature/add-to-namespaces! prefix new-n) 
               (str new-n "_" (to-identifier (last split)))))))))

(defn frag-without-prefix [uri]
  (cond (contains-string? uri "#") (last (clojure.string/split uri #"#"))
        (contains-string? uri "/") (last (clojure.string/split uri #"/"))
        :else                           (str "id_" (to-identifier uri))))

; ATTENTION unfrag doesn't work because to-identifier replaces a bunch of characters by "_" (which is not reversible)
; 
; (defn unfrag [string]
;   (let [split  (clojure.string/split string #"_")
;         prefix (first split)
;         namesp (get @signature/namespaces prefix (str prefix "_"))]
;    (str namesp (clojure.string/join "_" (rest split)))))
