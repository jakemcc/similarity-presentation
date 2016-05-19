(ns similarity.simhash
  (:require [clojure.java.io :as io]
            [clojure.string :as string]
            [similarity.core :as c]
            [similarity.cosine :as cosine]
            [similarity.hash :as hash]
            [similarity.jaccard :as jaccard])
  (:import java.awt.image.BufferedImage
           javax.imageio.ImageIO))

(defn similarity
  "Given two vectors of 0 or 1, returns similarity coefficent."
  [bits-a bits-b]
  {:pre [(= (count bits-a) (count bits-b))
         (every? (fn [b] (or (= b 0) (= b 1))) bits-a)
         (every? (fn [b] (or (= b 0) (= b 1))) bits-b)]}
  (let [xor (fn [b1 b2] (if (not= b1 b2) 1 0))]
    (- 1.0 (/ (count (filter pos? (map xor bits-a bits-b)))
              (count bits-a)))))


(comment
  ;; nearly similar
  (similarity [1 1 1 0 1 0 0 0 1 0]
              [1 1 1 0 1 0 0 0 1 1])

  ;; opposites
  (similarity [1 1 1 0 1 0 0 0 1 0]
              [0 0 0 1 0 1 1 1 0 1])
  )













































;; NEXT
(defn simhash
  [features]
  (let [n 64
        hashes (map hash/murmur features)
        combined-hashes (reduce (fn [r hash]
                                  (reduce (fn [r' i]
                                            (if (bit-test hash i)
                                              (update r' i inc)
                                              (update r' i dec)))
                                          r
                                          (range n)))
                                (vec (repeat n 0))
                                hashes)]
    (for [x combined-hashes]
      (if (<= 0 x)
        1
        0))))

(defn text-similarity
  [text-a text-b]
  (similarity (simhash (c/words text-a))
              (simhash (c/words text-b))))

(comment

  (text-similarity "The quick brown fox jumps over the lazy dog"
                   "The quick brown wolf jumps over the lazy dog")


  (text-similarity "The clown eats bacon"
                   "The bacon eats clown")



  )










































;; NEXT

(defn n-grams
  [n text]
  (->> text
       c/words
       (partition-all 2 1)
       (map (partial string/join " "))))

(defn text-similarity-n-grams
  [text-a text-b]
  (similarity (simhash (n-grams 2 text-a))
              (simhash (n-grams 2 text-b))))

(comment

  (text-similarity-n-grams "The quick brown fox jumps over the lazy dog"
                           "The quick brown wolf jumps over the lazy dog")

  (text-similarity-n-grams "The clown eats bacon"
                           "The bacon eats clown")




  )













































;; NEXT


(defn pixels
  [^BufferedImage buffered-image]
  (let [height (.getHeight buffered-image)
        width (.getWidth buffered-image)]
    (->> (.getPixels (.getData buffered-image) 0 0 width height (ints nil))
         (into [])
         ;; add rbg together to reduce features
         (partition 3)
         (mapv #(apply + %)))))

(defn image-similarity
  [image-a image-b]
  (let [bi-a (ImageIO/read (io/file image-a))
        bi-b (ImageIO/read (io/file image-b))]
    (similarity (simhash (pixels bi-a))
                (simhash (pixels bi-b)))))


(comment
  (image-similarity "images/jake-glasses-fence.png"
                    "images/jake-glasses-fence.png")
  
  (image-similarity "images/jake-glasses-fence-fixed.png"
                    "images/jake-glasses-fence.png")
  

  (image-similarity "images/jake-glasses-fence-fixed.png"
                    "images/water.jpg")  
  

  )














































































































































;; NEXT

(comment

  (def text1 "George Headley (1909–1983) was a West Indian cricketer who played 22 Test matches, mostly before the Second World War. Considered one of the best batsmen to play for West Indies and one of the greatest cricketers of all time, he also represented Jamaica and played professionally in England. Headley was born in Panama but raised in Jamaica where he quickly established a cricketing reputation as a batsman. West Indies had a weak cricket team through most of Headley's career; as their one world-class player, he carried a heavy responsibility, and they depended on his batting. He batted at number three, scoring 2,190 runs in Tests at an average of 60.83, and 9,921 runs in all first-class matches at an average of 69.86. He was chosen as one of the Wisden Cricketers of the Year in 1934.")

  (def text2 "George Headley was a West Indian cricketer who played 22 Test matches, mostly before the Second World War. Considered one of the best batsmen to play for West Indies and one of the greatest cricketers of all time, he also represented Jamaica and played professionally in England. Headley was born in Panama but raised in Jamaica where he quickly established a cricketing reputation as a batsman. West Indies had a weak cricket team through most of Headley's career; as their one world-class player, he carried a heavy responsibility, and they depended on his batting. He batted at number three, scoring 2,190 runs in tests at an average of 60.83, and 9,921 runs in all first-class matches at an average of 69.86. He was chosen as one of the Wisden Cricketers of the Year.")

  (count text1) ;; 791
  (count text2) ;; 771

  (jaccard/text-similarity text1 text2)
  (cosine/text-similarity text1 text2)
  (text-similarity text1 text2)
  (text-similarity-n-grams text1 text2)
  )
