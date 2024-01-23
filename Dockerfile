FROM openjdk:17-jdk

WORKDIR /opt

COPY target/*.jar budgeting-app.jar
COPY ./wait-for-it.sh wait-for-it.sh
RUN chmod +x wait-for-it.sh

ENV JAVA_TOOLS_OPTIONS -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5004

EXPOSE 8080

CMD ["java", "-jar", "budgeting-app.jar"]
