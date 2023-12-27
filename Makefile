.PHONY: unit-test
unit-test:
	clojure -M:tests:unit-test

.PHONY: kibit
kibit:
	clojure -M:tests:kibit src

.PHONY: kondo
kondo:
	clojure -M:tests:kondo --lint src --paralell

.PHONY: eastwood
eastwood:
	clojure -M:tests:eastwood

.PHONY: style-check
style-check:
	clojure -M:tests:style check

.PHONY: style-fix
style-fix:
	clojure -M:tests:style fix

.PHONY: build-uberjar
build-uberjar:
	clojure -T:build uber

.PHONY: run-uberjar
run-uberjar:
	java -jar target/cljamp.jar

.PHONY: build-docker
build-docker:
	docker build . -t cljamp

.PHONY: run-docker
run-docker:
	docker run -p 3000:3000 --name cljamp -d cljamp

.PHONY: run
run:
	clj -X main/-main