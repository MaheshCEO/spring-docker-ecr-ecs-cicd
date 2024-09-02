#use jdk image from docker hub
FROM openjdk:21
#Expose the port where spring boot application run on 
EXPOSE 8080
#Copy the  compiled jar jar file into container 
COPY .target/spring-docker-ecr-ecs-cicd-0.0.1-SNAPSHOT.jar ./app
#Command to run the application
ENTRYPOINT ["java", "-jar", "/spring-docker-ecr-ecs-cicd-0.0.1-SNAPSHOT.jar"]