# ProxyReencryptionGateway

How to start the ProxyReencryptionGateway application
---

1. Run `mvn clean install` to build your application
1. Start application with `java -jar target/pre-${version}.jar server src/main/resources/config.yml`
1. To check that your application is running enter url `http://localhost:7000`

Docker
---

1. Run `docker build -t freddielindsey/pre-gateway .`
1. Run `docker run -it freddielindsey/pre-gateway`

Health Check
---

To see your applications health enter url `http://localhost:7001/healthcheck`
