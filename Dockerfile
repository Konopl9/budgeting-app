FROM openjdk:17-jdk

WORKDIR /opt

COPY target/*.jar budgeting-app.jar

ENV JAVA_TOOLS_OPTIONS -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005

EXPOSE 8080

CMD ["java", "-jar", "market-data-service.jar"]