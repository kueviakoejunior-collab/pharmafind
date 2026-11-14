# ==============================================================================
# Dockerfile multi-stage pour PharmaFind (Java 21 / Spring Boot 3)
# Conçu pour un déploiement sécurisé, léger et hautement disponible
# ==============================================================================

# Étape 1 : Construction de l'artefact
FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /build

# Copie des fichiers de configuration Maven
COPY pom.xml ./
COPY src ./src

# Installation de Maven et compilation du JAR exécutable
RUN apk add --no-cache maven && \
    mvn clean package -DskipTests

# Étape 2 : Image d'exécution minimale
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Création d'un utilisateur non-root pour la sécurité
RUN addgroup -S pharma && adduser -S pharma -G pharma

# Copie du jar compilé depuis l'étape de build
COPY --from=builder /build/target/pharmafind-1.0.0.jar app.jar

# Droits et utilisateur
RUN chown -R pharma:pharma /app
USER pharma

# Variables d'environnement par défaut
ENV PORT=8080
ENV SPRING_PROFILES_ACTIVE=prod

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
