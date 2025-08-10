#!/bin/bash

# Start Spring Boot application in interactive mode
export APP_INTERACTIVE_MODE=true
./mvnw spring-boot:run -Dspring-boot.run.arguments="--app.interactive-mode=true"
