## clj-demos

Clojure project templates and sandboxing

### `setsail/`

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
* the
  [Om](https://github.com/omcljs/om)
  React wrapper

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

### `feels/`

express your
[feels](http://www.urbandictionary.com/define.php?term=Feels)
with handles to interactively update an
[emoji](https://en.wikipedia.org/wiki/Emoji)-like pictogram.

this project is based on a
[Chestnut](http://github.com/plexus/chestnut) 0.15.x
template and uses a
[system](https://github.com/danielsz/system)
of
[component](https://github.com/stuartsierra/component)s
to manage REPL state.

it currently demonstrates

* clojurescript's
  [support](https://clojurescript.org/reference/javascript-module-support)
  for javascript modules
* the
  [Rum](https://github.com/tonsky/rum)
  React wrapper
* [Citrus](https://github.com/roman01la/citrus),
  a Redux-like state management library for Rum
* [devcards](https://github.com/bhauman/devcards/)
  for a REPL-like UI development experience
* testing
  [ring sessions](https://github.com/ring-clojure/ring/wiki/Sessions)
  sans
  [peridot](https://github.com/xeqi/peridot)
  via
  [dynamic binding](https://www.braveclojure.com/zombie-metaphysics/#Dynamic_Binding)
* [doo](https://github.com/bensu/doo)
  tests for the frontend
* client-side routing with
  [bidi](http://github.com/juxt/bidi)
  and
  [accountant](http://github.com/venantius/accountant)


##### usage

build the javascript module dependency beforehand

```
feels$ npm run build
```

then run `lein repl` from a shell that has access to node
(in particular, run `nvm use` first if you use `nvm`).

see `feels/dev/user.clj` for the developer interface.
in particular,

* `(go)` starts the application on port `10555`
* `(go-devcards)` starts the "visual REPL" on `3449`

##### status

the frontend in particular could use some debug,
more complete tests, and styling
