# Similarity Presentation

Presentation and some supporting code for a presentation I gave on
text for similarity.

## Building

Presentation uses [cleaver](https://github.com/jdan/cleaver) for
generating the presentation.

`bin/watch` watches the `similarity.md` and builds `presentation.html` as changes happen.

`bin/build` builds `presentation.html` once.

## Viewing

`python -m SimpleHTTPServer` and then open [this](http://localhost:8000/presentation.html).

## References

Some references can be found in the `docs` directory. Some of them
were used for understanding the algorithm. Others I found after
writing this and thought they were useful and helped clarify ideas.
