# Keycloak: External User Storage SPI Example

This is a simple Keycloak user storage SPI to demonstrate how to implement a custom one

```shell
docker run -p 8888:8080 -e KEYCLOAK_ADMIN=admin -e KEYCLOAK_ADMIN_PASSWORD=change_me nivorbit_keycloak:1.0.0 --features=token-exchange,admin-fine-grained-authz,impersonation
```
