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
