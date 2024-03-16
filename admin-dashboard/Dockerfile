FROM openjdk:17

VOLUME /resources/images/

COPY target/tg-admin-dashboard.jar tg-admin-dashboard.jar

ENTRYPOINT ["java","-jar","/tg-admin-dashboard.jar"]

EXPOSE 8080