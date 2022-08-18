FROM quay.io/keycloak/keycloak:18.0.2 as builder

ARG JAR_FILE=target/*.jar

ENV KC_HEALTH_ENABLED=true
ENV KC_METRICS_ENABLED=true

# Install custom providers
COPY ${JAR_FILE} /opt/keycloak/providers/external-user-storage.jar
RUN /opt/keycloak/bin/kc.sh build

FROM quay.io/keycloak/keycloak:18.0.2
COPY --from=builder /opt/keycloak/ /opt/keycloak/
WORKDIR /opt/keycloak
# for demonstration purposes only, please make sure to use proper certificates in production instead
RUN keytool -genkeypair -storepass password -storetype PKCS12 -keyalg RSA -keysize 2048 -dname "CN=server" -alias server -ext "SAN:c=DNS:localhost,IP:127.0.0.1" -keystore conf/server.keystore
ENTRYPOINT ["/opt/keycloak/bin/kc.sh", "start-dev", "--features=token-exchange,admin-fine-grained-authz,impersonation"]