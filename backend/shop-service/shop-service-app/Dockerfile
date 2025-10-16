FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /app

# Tüm projeyi kopyala (çünkü parent pom ve diğer modüller lazım)
COPY . .

# Sadece user-service-app ve bağımlı modüllerini build et
RUN mvn -pl shop-service/shop-service-app -am clean package -DskipTests

# 2. Run aşaması
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Sadece app modülünün jar'ını al
COPY --from=build /app/shop-service/shop-service-app/target/shop-service-app-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
