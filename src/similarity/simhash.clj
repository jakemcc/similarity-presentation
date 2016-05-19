(ns similarity.simhash)

(defn similarity
  [bits-a bits-b]
  (- 1.0 (/ (count (filter true?
                           (map (fn [a b] (not= a b))
                                bits-a
                                bits-b)))
            (count bits-a))))
