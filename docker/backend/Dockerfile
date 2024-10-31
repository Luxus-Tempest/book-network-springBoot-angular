# Buil stage
FROM  maven:3.8.7-openjdk-18 AS build

# Set the working directory
WORKDIR /build

# Copy the source code dependancies in build directory
COPY pom.xml .

# Download Or Install the dependancies
# go-offline permet de télécharger les dépendances du projet meme on est hors ligne
RUN  mvn dependency:go-offline

# COPIER le code source (présent dans src --> le + important) dans le conteneur : src(du projet) --> /build/src
COPY src ./src

# Compiler le code source
RUN mvn clean package -DskipTests

# Runtime stage
FROM amazoncorretto:17

# Definir quelques arguments ou propriétés
ARG PROFILE=dev
ARG APP_VERSION=0.0.5-SNAPSHOT


WORKDIR /app

# Copier (vers /app) le fichier jar généré dans le conteneur a travers ->> ./mvnw clean package -DskipTests
COPY --from=build /build/target/book-network-api-*.jar /app/

# Ouvrir le port 8080 car notre application écoute sur ce port
EXPOSE 8088

# Definir les variables d'environnement
ENV ACTIVE_PROFILE=${PROFILE}
ENV JAR_VERSION=${APP_VERSION}

# LA BASE DE DONNEES :  jdbc:postgresql://localhost:5432/book_social_network
# Remplacer localhost par le nom du conteneur docker definir docker-compose.yml
ENV DB_URL=jdbc:postgresql://postgres-sql-bns:5432/book_social_network

# Commande pour lancer l'application

CMD java -jar -Dspring.profiles.active=${ACTIVE_PROFILE} -Dspring.datasource.url=${DB_URL}  book-network-api-${JAR_VERSION}.jar

#POUR VREER L'IMAGE DOCKER :: nom:bsn/bsn version(Tag):0.0.1-SNAPSHOT localisation_De_Dockerfile: ../docker/backend/Dockerfile
# A chaque fois que vous modifiez le code source, on ajuste  version pom.xml et on doit faire un nouveau build de l'image docker et ajuster le tag de l'image dans le docker-compose.yml
# docker build -t bsn/bsn:0.0.1-SNAPSHOT -f ../docker/backend/Dockerfile .