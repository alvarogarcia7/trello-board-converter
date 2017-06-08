.PHONY: exec-tests
exec-tests:
	java -jar ./target/uberjar/*standalone* "resources/trello2.json" > /tmp/trello2.actual
	diff /tmp/trello2.actual resources/trello2.expected.json
	echo "Tests are OK"
