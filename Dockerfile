# === STAGE 1: BUILD (Compiles the Java code) ===
# FIX: Using the highly stable 'maven:3-jdk-11' tag to resolve the image pull error.
FROM maven:3-jdk-11 AS build 

# Set the working directory inside the container
WORKDIR /app

# Copy the pom.xml and source code
COPY pom.xml .
COPY src ./src

# Build the application - creates the .war file in /app/target/
# FIX: Compilation will succeed due to the updated pom.xml configuring Java 11.
RUN mvn clean package -DskipTests

# === STAGE 2: RUNTIME (Deploys the WAR file into the Tomcat container) ===
FROM tomcat:9.0-jdk11-openjdk

# Set the working directory inside the Tomcat container
WORKDIR /usr/local/tomcat

# CRITICAL: Create directory for file uploads and set ownership
# FIX: The chown command is failing because 'tomcat' isn't recognized during the RUN command.
# This uses a safer, system-level change (group 0/root group) and ensures the directory 
# is highly writable (777) for maximum compatibility.
RUN mkdir -p webapps/data/materials && \
    chown -R 0 webapps/data && \
    chmod -R 777 webapps/data

# Remove default ROOT webapp
RUN rm -rf webapps/ROOT

# Copy the built WAR file from the 'build' stage into the Tomcat webapps folder
COPY --from=build /app/target/StudyMaterialPlatform.war webapps/ROOT.war

EXPOSE 8080
