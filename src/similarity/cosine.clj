(ns similarity.cosine
  (:require [clojure.string :as string]
            [similarity.core :as s]
            [clojure.set :as set])
  (:import java.lang.Math))

;; "raw" term frequency (no adjustments)
(defn term-frequency [text]
  (->> text
       (s/words)
       (frequencies)))

(defn square [x]
  (* x x))

(defn dot-product
  [a b]
  (let [ks (set/union (set (keys a))
                      (set (keys b)))]
    (reduce +
            (for [k ks]
              (* (get a k 0)
                 (get b k 0))))))

(defn text-similarity
  [text-a text-b]
  (let [tf-a (term-frequency text-a)
        tf-b (term-frequency text-b)]
    (/ (dot-product tf-a tf-b)
       (* (Math/sqrt (reduce + (map square (vals tf-a))))
          (Math/sqrt (reduce + (map square (vals tf-b))))))))

(comment
  (text-similarity "The quick brown dog jumps over the brown fox"
                   "The quick brown dog jumps over the brown fox")
  ;; 1.0
  
  (text-similarity "The quick brown dog jumps over the brown fox"
                   "The quick brown canine jumps over the brown fox")
  ;; 0.9090909090909091
  
  
  (text-similarity "The brown fox jumps quick over the sly wolf"
                   "The quick brown canine jumps over the brown fox")
  ;; 0.8040302522073697
  
  
  
  (text-similarity "Julie loves me more than Linda loves me"
                   "Jane likes me more than Julie loves me")
  
  
  )
