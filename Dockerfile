FROM tomcat:9.0-jdk11-openjdk

WORKDIR /usr/local/tomcat

# CRITICAL STEP: Create the directory for file uploads and ensure Tomcat has write access
# This matches the UPLOAD_DIRECTORY in MaterialController.java
RUN mkdir -p webapps/data/materials && \
    chown -R tomcat:tomcat webapps/data

# Remove default ROOT webapp
RUN rm -rf webapps/ROOT

# Copy the built WAR file from the Maven target directory to ROOT
COPY target/StudyMaterialPlatform.war webapps/ROOT.war

EXPOSE 8080