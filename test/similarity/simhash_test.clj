(ns similarity.simhash-test
  (:require[similarity.simhash :refer :all]
           [clojure.test :refer :all]))

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
