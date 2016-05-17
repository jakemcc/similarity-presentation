(ns similarity.core
  (:require [clojure.string :as string]))

(def dog "The quick brown dog jumps over the lazy fox")
(def doggie "The quick brown doggie jumps over the lazy fox")

(defn words [s]
  (string/split s #"\s+"))
