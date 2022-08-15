FROM openjdk:11

RUN mkdir /app
WORKDIR /app
COPY /target/pavershin-java-comptetion-1.9.jar /app/pavershin-java-comptetion-1.9.jar
EXPOSE 9081

ENTRYPOINT ["java", "-jar", "/app/pavershin-java-comptetion-1.9.jar"]