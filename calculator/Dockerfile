FROM openjdk:17-alpine
EXPOSE 8081
ARG JAR_FILE=build/libs/calculator-0.0.1.jar
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
