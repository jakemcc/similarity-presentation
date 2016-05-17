(ns similarity.cosine
  (:require [clojure.string :as string]
            [similarity.core :as s]
            [clojure.set :as set])
  (:import java.lang.Math))

(defmacro p [& body]
  `(let [x# ~@body]
     (println (str "dbg: " (quote ~@body) "=" x#))
     x#)
  )

;; "raw" term frequency (no adjustments)
(defn term-frequency [text]
  (->> text
       (s/words)
       (frequencies)))

(defn square [x]
  (* x x))

(defn dot-product
  [a b]
  (let [ks (set/union (set (keys a)) (set (keys b)))]
    (reduce +
            (for [k ks]
              (* (get a k 0)
                 (get b k 0))))))

(defn cosine-similarity
  "a and b are vectors from tf-idfs. Returns -1 for opposite, 1 for the same."
  [text-a text-b]
  (let [tf-a (term-frequency text-a)
        tf-b (term-frequency text-b)]
    (/ (dot-product tf-a tf-b)
       (* (Math/sqrt (reduce + (map square (vals tf-a))))
          (Math/sqrt (reduce + (map square (vals tf-b))))))))

;; TODO: I was trying to do a not words split (look at pairs) but was getting an exception.
;; TODO: implementing idf part of this doesn't work well with these examples.
;; TODO: try scaling TF by max frequency in document

(comment
  (let [[a b] (term-frequencies ["The quick brown dog jumps over the brown fox"
                                 "The quick brown canine jumps over the brown fox"])]
    (cosine-similarity a b))
  
  0.9090909090909091

  (let [[a b] (term-frequencies ["The brown fox jumps quick over the sly wolf"
                                 "The quick brown canine jumps over the brown fox"])]
    (cosine-similarity a b))
  
  
  
  (let [[a b] (term-frequencies ["Julie loves me more than Linda loves me"
                                 "Jane likes me more than Julie loves me"])]
    (cosine-similarity a b))
  0.8215838362577491

  )
