FROM java:openjdk-8-jdk

ADD target/pre-0.2.jar /data/pre.jar

ADD src/main/resources/config.yml /data/config.yml

WORKDIR /data

CMD java -jar pre.jar server /data/config.yml

EXPOSE 7000
