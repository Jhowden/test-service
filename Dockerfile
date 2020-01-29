FROM java:8-alpine
MAINTAINER Your Name <you@example.com>

ADD target/test-service-0.0.1-SNAPSHOT-standalone.jar /test-service/app.jar

EXPOSE 8080

CMD ["java", "-jar", "/test-service/app.jar"]
