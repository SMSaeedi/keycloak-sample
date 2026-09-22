# Keycloak-Spring

A Dockerized repo to maintain and manage users authorization and authentication
-----------------------------------------------

To be contributed to the concept and implementation, refer to the link below
-----------------------------------------------
[medium](https://medium.com/@d.navle95/add-users-to-keycloak-realm-with-spring-boot-and-perform-various-operations-879529ff7a9f)

## Requirements

- JDK 21 or newer
- Maven 3.9+
- Docker (for the containerized deployment)

## Build and run

```bash
mvn clean package
docker build -t keycloak-api .
```

The generated WAR is `target/keycloak-api.war`. Keycloak connection settings are supplied
through environment variables rather than source code:

| Variable | Default |
| --- | --- |
| `KEYCLOAK_SERVER_URL` | `http://sso-srv:1443` |
| `KEYCLOAK_REALM` | `master` |
| `KEYCLOAK_ADMIN_USERNAME` | `admin` |
| `KEYCLOAK_ADMIN_PASSWORD` | `admin` |
| `KEYCLOAK_CLIENT_ID` | `admin-cli` |
| `KEYCLOAK_CLIENT_SECRET` | empty |