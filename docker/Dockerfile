FROM eclipse-temurin:21
ARG JAR_FILE=build/libs/ticket.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]