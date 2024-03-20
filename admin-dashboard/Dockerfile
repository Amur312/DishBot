FROM openjdk:17

VOLUME /resources/images/

COPY target/admin-dashboard.jar admin-dashboard.jar

ENTRYPOINT ["java","-jar","/admin-dashboard.jar"]

EXPOSE 8080