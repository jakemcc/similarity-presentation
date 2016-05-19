(ns similarity.hash
  (:import com.google.common.base.Charsets
           com.google.common.hash.Hashing))

(defprotocol Murmur
  (-murmur [this]))

(def murmur-string
  (let [m (Hashing/murmur3_128)]
    (fn ^long [^String s]
      (-> (doto (.newHasher m)
            (.putString s Charsets/UTF_8))
          (.hash)
          (.asLong)))))

(def murmur-long
  (let [m (Hashing/murmur3_128)]
    (fn ^long [^Long s]
      (-> (doto (.newHasher m)
            (.putLong s))
          (.hash)
          (.asLong)))))

(extend-protocol Murmur
  String
  (-murmur [this]
    (murmur-string this))

  Integer
  (-murmur [this]
    (murmur-long (long this)))

  Long
  (-murmur [this]
    (murmur-long this)))

(defn murmur [x]
  (-murmur x))
