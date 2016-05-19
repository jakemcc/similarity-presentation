(ns similarity.simhash-test
  (:require [clojure.test :refer :all]
            [similarity.core :as c]
            [similarity.simhash :refer :all]))

(defn bit-vector [& args]
  (vec (concat (repeat (- 128 (count args)) 0) args)))

(deftest test-similarity
  (is (= 1.0 (similarity (bit-vector 0 1 1) (bit-vector 0 1 1))))
  (is (= (/ 126.0 128.0) (similarity (bit-vector 0 1)
                                     (bit-vector 1 0))))
  (is (= (/ 125.0 128.0) (similarity (bit-vector 0 0 0)
                                     (bit-vector 1 1 1))))
  (is (= (similarity (bit-vector 0 0 0) (bit-vector 1 1 1))
         (similarity (bit-vector 1 1 1) (bit-vector 0 0 0))))
  (is (= 0.75 (similarity (apply bit-vector (repeat 32 1))
                          (bit-vector 0)))))


(deftest test-text-similarity
  (is (= 1.0 (text-similarity "This is a sentence"
                              "This is a sentence"))))


(deftest test-image-similarity
  (is (= 1.0 (image-similarity "images/jake-glasses-fence-fixed.png"
                               "images/jake-glasses-fence-fixed.png")))
  
  (is (< 0.80
         (image-similarity "images/jake-glasses-fence-fixed.png"
                           "images/jake-glasses-fence.png")
         0.98))
  
  (is (not= 1.0 (image-similarity "images/jake-glasses-fence-fixed.png"
                                  "images/water.jpg"))))



