#FROM  tomcat:9.0.70-jdk11-corretto-al2
FROM  tomcat:9.0.107
USER root
ARG DEBIAN_FRONTEND=noninteractive
#RUN apt-get update \
#    && apt-get -yq  install apt-utils \
#    && apt-get -yq  install curl \
#    && apt-get -yq  install telnet \
#    && apt-get -yq  install net-tools \
#    && apt-get install -y ca-certificates-java

ADD target/keycloak-api.war /usr/local/tomcat/webapps/
ENV TZ=Asia/Tehran
EXPOSE 8080 8000 1616
CMD ["catalina.sh", "jpda", "run"]

# sudo docker build . -t  repo.roham.co.ir/repository/docker-registry/keycloak-api:BUILD_NUMBER