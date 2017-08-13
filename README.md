### clj-demos

Clojure project templates and sandboxing

##### `setsail/`

extracted from [emo-mcg](http://github.com/ransomw/emo-mcg),
this project was originally derived from the
[Chestnut](https://github.com/plexus/chestnut) 0.14.0 (66af6f40)
template.

to give it a spin, make certain
[leiningen](https://leiningen.org)
is installed, and at the repl prompt offered by

```
clj-demos/setsail$ lein repl
```

run the functions defined in `dev/user.clj`

```
(rdb)
(run)
```

to reset the database and run the application via
[figwheel](https://github.com/bhauman/lein-figwheel).
then point a browser to `localhost:3449`.

_status_ ~ the extraction remains very direct:
all that's done so far is chucking out the parts of the emo-mcg project
that weren't related to the demo and updating the database reset.
