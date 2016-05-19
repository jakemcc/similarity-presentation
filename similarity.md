

# Similarity

# Motivation

The same content gets republished across different web properties.

Lumanu doesn't want to show the same dog story twenty times on the dashboard.

Google News doesn't want to show the same story multiple times.

Republished content isn't always exactly the same.

# Approaches

# Approaches : Equality or Checksums

Clearly works when strings are exactly the same. Fails otherwise.

```clojure
(= "The quick dog jumps over the lazy fox" "The quick dog jumps over the lazy fox")
```

# Approaches : Jaccard index 

Also known as **Jaccard similarity coefficent.**

Tells you the similarity between two sets.

Need to make text into sets.


# Approaches : Cosine Similarity

> Cosine similarity is a measure of similarity between two vectors of
> an inner product space that measures the cosine of the angle between
> them. - Wikipedia

Need to transform your documents into a vector of weights. One way of
doing this is tf-idf (term frequency * inverse document frequency).

# Term Frequency

# Term Frequency : Most basic form

count of each word in a document.

```clojure
(frequency (words text))
```

# Term Frequency : Less basic

Adjust by max frequency in document. Adjusts for document length.

```clojure
(let [tfs (frequency (words text))
      max-f (apply max (vals tfs)]
  (update-vs tfs (fn [x] (+ 0.5 (* 0.5 (/ x max-f)))))))
```

# Inverse Document Frequency

Discounts terms that appear in many of the documents.

```clojure
(defn idf [term texts]
  (Math/log (/ (count texts)
               (count (filter #(.contains % term)
                              texts)))))
```

# Term frequency-Inverse document frequency

Multiply the term frequency by the inverse document frequency for a term.

# Approaches : Cosine Similarity

You've transformed your documents into vectors. Now take the cosine similarity 

# Cosine Similarity : Downsides

- Depending on your vectorization step you might need to inspect the
  whole corpus every time
- Need to store vectorized form of each document
- Have to compare each document to all other documents (n * n)


# Goals of new approach

1. New documents don't require recalculation of previous work (so no idf)
2. Store less data (no tf-idf per document)
3. Don't compare each document to all other documents

# Goal 1: No recalulation

- [Locality Senstive Hashing](https://en.wikipedia.org/wiki/Locality-sensitive_hashing) is a type of hashing
  where similar items hash to nearly the same value.
- Can think of it as maximizing collisions.
- [SimHash](http://www.wwwconference.org/www2007/papers/paper215.pdf) is one specific LSH.
- Just storing a hash actuall solves *Goal 2: Store less data* as well.

# SimHash

- Created by Moses Charikar -
  [Similarity estimation techniques from rounding algorithrms](https://dl.acm.org/citation.cfm?doid=509907.509965)
- Designed to approximate the cosine distance between vectors!
- Shown to be useful for near-duplicate detection
  [Detecting Near-Duplictes for Web Crawling](http://www.wwwconference.org/www2007/papers/paper215.pdf)

# SimHash Algorithm (as applied to articles)

1. Create a `f` elements sized vector initialzed to all `0`
1. Break article into set of features
1. Hash each feature into `f` bit sized value
1. For each hash: 
   if bit `i` >= 0: then vector[i]++, otherwise vector[i]--
1. Iterate elements in vector. if vector[i] >= 0, final hash bit `i`
   is `1`; otherwise bit `i` is `0`

# Comparing SimHashes

1. xor two f-bit simhashes together
1. Count number of `1` result from above step.
1. Divide by number of bits, `f` to get dissimilarity
1. 1.0 - dissimilarity = similarity

# Example

10 bit simhashes
```
1001101101 xor
1001101011
----------
0000000110

1 - (2 / 10) = 0.80 similar
```

# Goal 3: Don't compare single document to all other documents

See
[Detecting Near-Duplictes for Web Crawling](http://www.wwwconference.org/www2007/papers/paper215.pdf)
and [Blog post](http://matpalm.com/resemblance/simhash/)

**Summary**: Take advantage of manipulating bits of hashes and sorting
to minimize comparisons.

# Example

Cherry picked example: Two articles tagged as 79% similar.

https://app.lumanu.com/?gs=5d3318be1c7397b7db415af8186ecb6c2fc037a0&gs=9a69de1b1d232462d0327bb1c3670a2dd0199576



