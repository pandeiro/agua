# agua

Utilities for scripting simple "isomorphic" webapps with ClojureScript and React
in a single [Boot](http://boot-clj.com) file.

[](dependency)
```clojure
[pandeiro/agua "0.1.0"] ;; latest release
```
[](/dependency)

The tooling **includes React (with reagent)** to facilitate view
rendering and **automatically prerenders dynamic views on the server**
in order to serve HTML to non-JS-enabled browsers, web crawlers, etc.

## Usage

See this [gist](https://gist.github.com/c5d2728bd04aab4f31c3) for a
working example complete with **routing** and **client-side-only
behavior**.

### API

#### Helper Macros

- `defhtml` - Define an HTML string as Hiccup vectors (already includes doctype and root html node for you)
- `defcljs` - Define a React/reagent-based ClojureScript app to be rendered server- and client-side
- `defcss`  - Define CSS styles as Garden vectors and maps

#### Main Function

- `serve`  - Serves the application by prerendering its view and including styles and scripts inline in the HTML

## License

Copyright © 2015 Murphy McMahon

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
