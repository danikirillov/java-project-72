run-dist:
	./build/install/app/bin/app -f plain src/test/resources/fixtures/wellFormedPlain1.yaml src/test/resources/fixtures/wellFormedPlain2.yml

build:
	./gradlew clean build

run:
	./gradlew run

test:
	./gradlew test

report:
	./gradlew jacocoTestReport

lint:
	./gradlew checkstyleMain

.PHONY: build