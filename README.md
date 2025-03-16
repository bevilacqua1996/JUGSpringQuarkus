# JUGSpringQuarkus
This is a repository to store all documentation and implementation which I have been done for Lisbon JUG presentation that discuss the difference in terms of performance between Spring-Boot and Quarkus

## Project Architecture

![Architecture_practical_part-Docker-compose infrastructure (1)](https://github.com/user-attachments/assets/ec65c16d-a4af-498c-8e4c-36d82b3c32e7)

In order to build up the project on your machine you must have docker and docker-compose installed. 

[Docker install tutorial](https://docs.docker.com/engine/install/ubuntu/)

In order to start up the compose project on your machine, execute this command in the source folder:

`docker-compose up -d --build`

Also, in order to build the non-native images you need to execute:

`./mvnw clean package`

This one will build Quarkus or Spring-Boot applications. Then to build the docker image:

`docker build -t your-image-name:latest .`

For native images, you need a different command to build the application:

`./mvnw clean package -Pnative -Dquarkus.native.container-build=true`

This one will build the application as a native java. This one, runs the build process in a specific container that has all the requirements installed already.

## Native Image build strategy in GitHub Actions

![Architecture_practical_part-Native image generation](https://github.com/user-attachments/assets/f702c215-c810-4b11-aaaa-77d0315b2d74)

Besides the JVM components for Spring and Quarkus we also will have the Native Java ones.
As these ones demands more memory during build process, the process was automated in a GitHub Actions pipeline as the image below shows
