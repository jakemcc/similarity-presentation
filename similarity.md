title: Similarity
author:
  name: Jake McCrary
  twitter: jakemcc
  url: https://jakemccrary.com
  email: jake@jakemccrary.com
style: ./similarity.css
controls: false
output: presentation.html

---

# Similarity
## Jake McCrary

---

# Motivation

<!-- --- -->

<!-- ### Motivation -->

<!-- The web is highly repetitive. -->

---

# Approaches

---

# Equality or checksums

---

<!-- ### Approaches : Jaccard index  -->

<!-- AKA: **Jaccard similarity coefficient.** -->

<!-- Tells you the similarity between two sets. -->

<!-- Need to make text into sets. -->

<!-- --- -->

# Cosine Similarity

--- 

### Approaches: Cosine Similarity

> Cosine similarity is a measure of similarity between two vectors of
> an inner product space that measures the cosine of the angle between
> them. - Wikipedia

---

### Approaches: Cosine Similarity

![](/images/vectors.png)

---
 
### Approaches: Cosine Similarity

1. Transform documents into vectors.

---

### Approaches: Cosine Similarity

1. Transform documents into vectors.
1. Calculate the cosine of the angle between vectors.

---

# Vectorizing

---

# `tf-idf`

---

### Vectorizing: Term Frequency

Most basic form:

```clojure
(frequency (words text))
```

---

### Vectorizing: Term Frequency

Fancier, adjust by max frequency.


```clojure
(let [tfs (frequency (words text))
      max-f (apply max (vals tfs))]
  (update-vs tfs (fn [f] (+ 0.5 (* 0.5 (/ f max-f))))))
```

---

### Vectorizing: Inverse Document Frequency

Terms that are common across corpus are discounted

```clojure
(defn idf [term texts]
  (Math/log (/ (count texts)
               (count (filter #(string/includes? % term)
                              texts)))))
```

---

### Vectorizing: tf-idf

Multiply term frequency by inverse document frequency

---

### Approaches: Cosine Similarity


```clojure
(defn text-similarity
  [text-a text-b]
  (let [tf-a (term-frequency text-a)
        tf-b (term-frequency text-b)]
    (/ (dot-product tf-a tf-b)
       (* (Math/sqrt (reduce + (map square (vals tf-a))))
          (Math/sqrt (reduce + (map square (vals tf-b))))))))
```

---

### Approaches: Cosine Similarity

```clojure
(text-similarity "The quick brown dog jumps over the brown fox"
                 "The quick brown dog jumps over the brown fox")
```

---

### Approaches: Cosine Similarity

```clojure
(text-similarity "The quick brown dog jumps over the brown fox"
                 "The quick brown dog jumps over the brown fox")
;; 1.0
```

---

### Approaches: Cosine Similarity

```clojure
(text-similarity "The quick brown dog jumps over the brown fox"
                 "The quick brown dog jumps over the brown fox")
;; 1.0

(text-similarity "The quick brown dog jumps over the brown fox"
                 "The quick brown canine jumps over the brown fox")
```

---

### Approaches: Cosine Similarity

```clojure
(text-similarity "The quick brown dog jumps over the brown fox"
                 "The quick brown dog jumps over the brown fox")
;; 1.0

(text-similarity "The quick brown dog jumps over the brown fox"
                 "The quick brown canine jumps over the brown fox")
;; 0.9090909090909091
```

---

### Approaches: Cosine Similarity

```clojure
(text-similarity "The quick brown dog jumps over the brown fox"
                 "The quick brown dog jumps over the brown fox")
;; 1.0

(text-similarity "The quick brown dog jumps over the brown fox"
                 "The quick brown canine jumps over the brown fox")
;; 0.9090909090909091

(text-similarity "The brown fox jumps quick over the sly wolf"
                 "The quick brown canine jumps over the brown fox")
```

---

### Approaches: Cosine Similarity

```clojure
(text-similarity "The quick brown dog jumps over the brown fox"
                 "The quick brown dog jumps over the brown fox")
;; 1.0

(text-similarity "The quick brown dog jumps over the brown fox"
                 "The quick brown canine jumps over the brown fox")
;; 0.9090909090909091

(text-similarity "The brown fox jumps quick over the sly wolf"
                 "The quick brown canine jumps over the brown fox")
;; 0.8040302522073697
```

---

### Cosine Similarity: Downsides

- Vectorization step might depend on whole corpus

---

### Cosine Similarity: Downsides

- Vectorization step might depend on whole corpus
- Need to store vectorized form of each document

---

### Cosine Similarity: Downsides

- Vectorization step might depend on whole corpus
- Need to store vectorized form of each document
- Comparison step is O(n<sup>2</sup>)

---

### Goals of new approach

1. New documents don't require recalculation of previous work
1. Store less data
1. Comparison step is better than O(n<sup>2</sup>)

---

### Goal 1: No recalculation

---

### Goal 1: No recalculation

[Locality Sensitive Hashing](https://en.wikipedia.org/wiki/Locality-sensitive_hashing)

---

### Goal 1: No recalculation

Just storing a hash actually solves **Goal 2: Store less data** as well

---

# SimHash

---

### SimHash

- [Similarity estimation techniques from rounding algorithms](http://www.cs.princeton.edu/courses/archive/spring04/cos598B/bib/CharikarEstim.pdf)
  by Moses Charikar


---

### SimHash

- [Similarity estimation techniques from rounding algorithms](http://www.cs.princeton.edu/courses/archive/spring04/cos598B/bib/CharikarEstim.pdf)
  by Moses Charikar
- Designed to approximate the cosine distance between vectors!

---

### SimHash

- [Similarity estimation techniques from rounding algorithms](http://www.cs.princeton.edu/courses/archive/spring04/cos598B/bib/CharikarEstim.pdf) by Moses Charikar
- Designed to approximate the cosine distance between vectors!
- Shown to be useful for near-duplicate detection
  [Detecting Near-Duplicates for Web Crawling](http://www.wwwconference.org/www2007/papers/paper215.pdf)

---


### SimHash Algorithm

1. Create a `f` sized vector initialized to all `0`

---

### SimHash Algorithm

1. Create a `f` sized vector initialized to all `0`
1. Break article into set of features

---

### SimHash Algorithm

1. Create a `f` sized vector initialized to all `0`
1. Break article into set of features
1. Hash each feature into `f` bit sized value

---

### SimHash Algorithm

1. Create a `f` sized vector initialized to all `0`
1. Break article into set of features
1. Hash each feature into `f` bit sized value
1. For each bit in each hash  
   `if bit[i] == 1 then vec[i]++ otherwise vec[i]--`

---

### SimHash Algorithm

1. Create a `f` sized vector initialized to all `0`
1. Break article into set of features
1. Hash each feature into `f` bit sized value
1. For each bit in each hash  
   `if bit[i] == 1 then vec[i]++ otherwise vec[i]--`
1. For each element in vec:  
   `if elem >= 0 then 1 otherwise 0`

-- larger-code

### SimHash Algorithm: Result

```
1001101011
```

---

### Comparing SimHashes

1. xor simhashes

---

### Comparing SimHashes

1. xor simhashes
1. Count the number of bits set to `1` in xor result

---

### Comparing SimHashes

1. xor simhashes
1. Count the number of bits set to `1` in xor result
1. Divide by number of bits, `f`, to get dissimilarity

---

### Comparing SimHashes

1. xor simhashes
1. Count the number of bits set to `1` in xor result
1. Divide by number of bits, `f`, to get dissimilarity
1. 1.0 - dissimilarity = similarity

-- larger-code

### Example

```
1001101101 xor
1001101011 
```

-- larger-code

### Example

```
1001101101 xor
1001101011 
==========
0000000110
```

-- larger-code

### Example

```
1001101101 xor
1001101011 
==========
0000000110
```

```
1 - (2 / 10) = 0.8 similar
```

---


### Goal 3: Don't compare single document to all other documents

---

### Goal 3: Don't compare single document to all other documents

**Summary**: Take advantage of manipulating bits of hashes and sorting
to minimize comparisons.

See
[Detecting Near-Duplicates for Web Crawling](http://www.wwwconference.org/www2007/papers/paper215.pdf)
and [blog post](http://matpalm.com/resemblance/simhash/)

---

### Example

Cherry picked example:
[Two articles](https://app.lumanu.com/?gs=5d3318be1c7397b7db415af8186ecb6c2fc037a0&gs=9a69de1b1d232462d0327bb1c3670a2dd0199576)
tagged as 79% similar.

