FROM tomcat:10.1-jdk21-temurin
USER root
COPY target/keycloak-api.war /usr/local/tomcat/webapps/
ENV TZ=Asia/Tehran
EXPOSE 8080 8000 1616
CMD ["catalina.sh", "jpda", "run"]