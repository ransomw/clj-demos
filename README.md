### clj-demos

Clojure project templates and sandboxing

##### `setsail/`

extracted from [emo-mcg](http://github.com/ransomw/emo-mcg),
this project was originally derived from the
[Chestnut](http://github.com/plexus/chestnut) 0.14.0 (66af6f40)
template.

it demonstrates
* backend Create and Read with
  [compojure](http://weavejester.github.io/compojure/)
  and
  [hugsql](http://www.hugsql.org/),
  using
  [edn](http://github.com/edn-format/edn)
  to communicate with
  [cljs-http](http://github.com/r0man/cljs-http)
  on the frontend
* a handrolled
  [flux](http://facebook.github.io/flux/docs/overview.html)
  loop implemented with Clojure's
  [core.async](http://github.com/clojure/core.async)
  (a [CSP](http://en.wikipedia.org/wiki/Communicating_sequential_processes)
  implementation similar to
  [goroutines](http://en.wikipedia.org/wiki/Go_(programming_language)#Concurrency:_goroutines_and_channels))
  and inspired by
  [alt.js](http://alt.js.org/guide/)
* markdown and styling (nearly) pushed into
  [S-expressions](http://en.wikipedia.org/wiki/S-expression)
  via
  [sablono](http://github.com/r0man/sablono)
  and
  [garden](http://github.com/noprompt/garden)
* `src/clj/garden_build/` — a simple sandboxed styles build
  similar to
  [garden-watcher](http://github.com/plexus/garden-watcher)

it is _not_ suitable as an application template due to (at least)
* patterns for [figwheel](http://github.com/bhauman/lein-figwheel)
  reloading
* remaining cruft (e.g. `project.clj`) from the original extraction

to give it a spin,
check your database setup against
`setsail/env/test/clj/setsail/config.clj`,
make certain
[leiningen](http://leiningen.org)
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
[figwheel](http://github.com/bhauman/lein-figwheel).
then point a browser to `localhost:3449`.
