# === STAGE 1: BUILD (Uses Maven/JDK image to compile the Java code) ===
FROM maven:3.8.7-jdk-11 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the pom.xml and source code
COPY pom.xml .
# The dot copies everything else, including the 'src' folder
COPY src ./src

# Build the application - creates the .war file in /app/target/
# The 'clean' phase removes old builds, 'package' creates the WAR
RUN mvn clean package -DskipTests

# === STAGE 2: RUNTIME (Uses the smaller Tomcat image for final deployment) ===
FROM tomcat:9.0-jdk11-openjdk

# Set the working directory inside the Tomcat container
WORKDIR /usr/local/tomcat

# CRITICAL STEP: Create the directory for file uploads and ensure Tomcat has write access
# This matches the UPLOAD_DIRECTORY in MaterialController.java
RUN mkdir -p webapps/data/materials && \
    chown -R tomcat:tomcat webapps/data

# Remove default ROOT webapp
RUN rm -rf webapps/ROOT

# **FIX:** Copy the built WAR file from the 'build' stage into the Tomcat webapps folder
# The path is now FROM the previous stage, not the host machine.
COPY --from=build /app/target/StudyMaterialPlatform.war webapps/ROOT.war

EXPOSE 8080

# Tomcat entrypoint runs automatically
