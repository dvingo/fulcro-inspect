tests:
	yarn install
	lein doo chrome automated-tests once
electron-dev:
	(cd shells/electron; electron .)
