# === STAGE 1: BUILD (Compiles the Java code) ===
# Using the confirmed existing 'slim' tag for guaranteed image pull
FROM maven:3.8.7-jdk-11-slim AS build 

# Set the working directory inside the container
WORKDIR /app

# Copy the pom.xml and source code
COPY pom.xml .
COPY src ./src

# Build the application - creates the .war file in /app/target/
RUN mvn clean package -DskipTests

# === STAGE 2: RUNTIME (Deploys the WAR file into a small Tomcat image) ===
FROM tomcat:9.0-jdk11-openjdk

# Set the working directory inside the Tomcat container
WORKDIR /usr/local/tomcat

# CRITICAL: Create directory for file uploads and set ownership
RUN mkdir -p webapps/data/materials && \
    chown -R tomcat:tomcat webapps/data

# Remove default ROOT webapp
RUN rm -rf webapps/ROOT

# Copy the built WAR file from the 'build' stage into the Tomcat webapps folder
COPY --from=build /app/target/StudyMaterialPlatform.war webapps/ROOT.war

EXPOSE 8080
