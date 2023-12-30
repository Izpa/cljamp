.PHONY: unit-test
unit-test:
	clojure -M:run-test:unit-test unit-test

.PHONY: integration-test
integration-test:
	clojure -M:run-test:integration-test integration

.PHONY: e2e-test
e2e-test:
	clojure -M:run-test:e2e-test e2e

.PHONY: kibit
kibit:
	clojure -M:test-paths:kibit src

.PHONY: kondo
kondo:
	clojure -M:test-paths:kondo --lint src --paralell

.PHONY: eastwood
eastwood:
	clojure -M:test-paths:eastwood

.PHONY: style-check
style-check:
	clojure -M:test-paths:style check

.PHONY: style-fix
style-fix:
	clojure -M:test-paths:style fix

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