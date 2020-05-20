To start work:


```bash
./script/start_dev.sh
```

this runs:
```bash
yarn
yarn shadow-cljs watch chrome-devtool chrome
```
The creates output in `shells/chrome`

in chrome:

chrome://extensions/

Developer mode on
Load unpacked

The extension doesn't load in brave due to some security issue around resource
loading. will need to look into it.

# DB explorer
this is the first tab
fulcro.inspect.ui.db-explorer

# implementation notes

Main UI class:

fulcro.inspect.ui.multi-inspector
