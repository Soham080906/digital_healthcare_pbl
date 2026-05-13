FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY . .

RUN chmod +x mvnw
RUN ./mvnw clean install -DskipTests

CMD ["java", "-jar", "target/digital_healthcare-0.0.1-SNAPSHOT.jar"]