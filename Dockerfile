# Stage 1: build the WAR with Maven
#FROM maven:3.9-eclipse-temurin-17 AS build
#WORKDIR /app
#COPY ready2read/ .

# Write db.properties pointing to the 'db' compose service
#RUN printf 'db.url=jdbc:mysql://db:3306/ready2read\ndb.user=ready2read\ndb.password=ready2read\n' \
   # > src/main/resources/db.properties

#RUN mvn clean package -DskipTests

# Stage 2: deploy to Tomcat
#FROM tomcat:10.1-jdk17
#RUN rm -rf /usr/local/tomcat/webapps/*
#COPY --from=build /app/target/ready2read.war /usr/local/tomcat/webapps/ROOT.war
#EXPOSE 8080
FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /app
COPY ready2read/ .

RUN mkdir -p src/main/resources && \
    printf 'db.url=jdbc:mysql://db:3306/ready2read\ndb.user=ready2read\ndb.password=ready2read\n' > src/main/resources/db.properties

RUN mvn clean package -DskipTests

FROM tomcat:10.1-jdk17

RUN rm -rf /usr/local/tomcat/webapps/*
COPY --from=build /app/target/ready2read.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080
