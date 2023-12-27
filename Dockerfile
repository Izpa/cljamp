FROM openjdk:11

WORKDIR /

COPY target/cljamp.jar cljamp.jar
EXPOSE 3000

CMD java -jar cljamp.jar