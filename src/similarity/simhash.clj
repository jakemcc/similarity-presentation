(ns similarity.simhash)

(defn similarity
  "Given two vectors of 0 or 1, returns similarity coefficent."
  [bits-a bits-b]
  {:pre [(= (count bits-a) (count bits-b))]}
  (let [xor (fn [b1 b2] (if (not= b1 b2) 1 0))]
    (- 1.0 (/ (count (filter (partial = 1)
                             (map xor bits-a bits-b)))
              (count bits-a)))))


(comment
  (similarity [1] [0 0])

  )
