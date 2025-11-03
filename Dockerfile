# === STAGE 1: BUILD (Compiles the Java code) ===
FROM maven:3-jdk-11 AS build 

# Set the working directory inside the container
WORKDIR /app

# Copy the pom.xml and source code
COPY pom.xml .
COPY src ./src

# Build the application - creates the .war file in /app/target/
RUN mvn clean package -DskipTests

# === STAGE 2: RUNTIME (Deploys the WAR file into the Tomcat container) ===
FROM tomcat:9.0-jdk11-openjdk

# Set the working directory inside the Tomcat container
WORKDIR /usr/local/tomcat

# CRITICAL: Create directory for file uploads and set ownership
# The chown fix remains in place
RUN mkdir -p webapps/data/materials && \
    chown -R 0 webapps/data && \
    chmod -R 777 webapps/data

# Remove default ROOT webapp
RUN rm -rf webapps/ROOT

# FIX: Copy the built WAR file using the correct Maven filename (including version).
COPY --from=build /app/target/StudyMaterialPlatform-1.0-SNAPSHOT.war webapps/ROOT.war

EXPOSE 8080
