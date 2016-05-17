(ns similarity.jaccard
  (:require [similarity.core :refer :all]
            [clojure.set :as set]
            [clojure.string :as string]))


(defn jaccard-distance [a b]
  (let [count-union (count (set/union a b))
        count-intersection (count (set/intersection a b))]
    (/ (- count-union count-intersection)
       count-union)))

(defn jaccard-similarity [a b]
  (- 1.0 (jaccard-distance a b)))







(comment
  ;; the same
  (jaccard-similarity #{1 2 3 4 5 6 7 8 9 10}
                      #{1 2 3 4 5 6 7 8 9 10})

  ;; one less in second set
  (jaccard-similarity #{1 2 3 4 5 6 7 8 9}
                      #{1 2 3 4 5 6 7 8 9 10})

  ;; add one 
  (jaccard-similarity #{1 2 3 4 5 6 7 8 9 10}
                      #{1 2 3 4 5 6 7 8 9 10 11})

  ;; completely different
  (jaccard-similarity #{1 2 3 4 5 6 7 8 9 10}
                      #{11 12 13 14})
  )








(defn text-similarity [string-a string-b]
  (jaccard-similarity (set (words string-a))
                      (set (words string-b))))

(comment
  (text-similarity "Hello" "Hello")

  (text-similarity "The rabbit wins the race"
                   "The rabbit loses the race")

  (text-similarity dog doggie)

  (text-similarity "The quick brown FOX jumps over the lazy DOG"
                   "The quick brown DOG jumps over the lazy FOX")
  )








(defn text-similarity2 [string-a string-b]
  (jaccard-similarity (set (partition-all 2 1 (words string-a)))
                      (set (partition-all 2 1 (words string-b)))))



(comment

  (text-similarity2 "The quick brown FOX jumps over the lazy DOG"
                    "The quick brown DOG jumps over the lazy FOX")
  
  
  

  )


