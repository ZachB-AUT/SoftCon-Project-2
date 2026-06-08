# Makefile for HealthDataManager
# Must be run from the HealthDataManager directory (alongside pom.xml)
# Usage: make [run|build|clean|test]

.PHONY: run build clean test

# Compile and launch via Maven so that javatypst and all other
# dependencies are on the classpath and native libraries are loaded correctly.
run:
	mvn javafx:run

# Package the project into a JAR (output goes to target/)
build:
	mvn package

# Remove all Maven build artefacts
clean:
	mvn clean

# Compile and run the test suite
test:
	mvn test