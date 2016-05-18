

# Similarity

# Motivation

The same content gets republished across different web properties.

Lumanu doesn't want to show the same dog story twenty times on the dashboard.

Google News doesn't want to show the same story multiple times.

Republished content isn't always exactly the same.

# Approaches

# Approaches : Equality

Clearly works when strings are exactly the same. Fails otherwise.

```clojure
(= "The quick dog jumps over the lazy fox" "The quick dog jumps over the lazy fox")
```

# Approaches : Jaccard index 

Also known as Jaccard similarity coefficent.

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


# Simhash

http://www.wwwconference.org/www2007/papers/paper215.pdf
