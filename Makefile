.PHONY: test
test:
	java -jar ./target/uberjar/*standalone* "resources/trello2.json" > /tmp/trello2.actual
	diff /tmp/trello2.actual resources/trello2.expected.json
	echo "Tests are OK"

.PHONY: build
build:
	lein uberjar
	mkdir -p build; echo "Created folder"
	cp target/uberjar/*standalone* build/trello-board-converter.jar

.PHONY: run
run:
	java -jar build/trello-board-converter.jar "books.json" >> books.md

