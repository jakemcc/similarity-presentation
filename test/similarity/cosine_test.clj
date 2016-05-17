(ns similarity.cosine-test
  (:require [similarity.cosine :refer :all]
            [clojure.test :refer :all]))

(defn about= [x y]
  (<= (Math/abs (- y x)) 0.00001))

(deftest test-cosine-similarity
  (is (about= 0.909090 (cosine-similarity "The quick brown dog jumps over the brown fox"
                                          "The quick brown canine jumps over the brown fox")))

  (is (about= 1.0 (cosine-similarity "The quick brown dog jumps over the brown fox"
                                     "The quick brown dog jumps over the brown fox"))))
