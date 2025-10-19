#FROM maven:3.9.9-eclipse-temurin-21 AS build
#
#WORKDIR /app
#
## Tüm projeyi kopyala (çünkü parent pom ve diğer modüller lazım)
#COPY . .
#
## Sadece user-service-app ve bağımlı modüllerini build et
#RUN mvn -pl warehouse-service/warehouse-service-app -am clean package -DskipTests
#
## 2. Run aşaması
#FROM eclipse-temurin:21-jre-alpine
#WORKDIR /app
#
## Sadece app modülünün jar'ını al
#COPY --from=build /app/warehouse-service/warehouse-service-app/target/warehouse-service-app-0.0.1-SNAPSHOT.jar app.jar
#
#EXPOSE 8080
#ENTRYPOINT ["java","-jar","app.jar"]
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

# 1. POM dosyaları
COPY pom.xml .
COPY common/pom.xml common/
COPY user-service/user-service-app/pom.xml user-service/user-service-app/
COPY user-service/user-service-client/pom.xml user-service/user-service-client/
COPY shop-service/shop-service-app/pom.xml shop-service/shop-service-app/
COPY shop-service/shop-service-client/pom.xml shop-service/shop-service-client/
COPY api-gateway/pom.xml api-gateway/
COPY warehouse-service/warehouse-service-app/pom.xml warehouse-service/warehouse-service-app/
COPY warehouse-service/warehouse-service-client/pom.xml warehouse-service/warehouse-service-client/

# 2. Bağımlılıkları indir
RUN --mount=type=cache,target=/root/.m2 \
    mvn -pl warehouse-service/warehouse-service-app -am dependency:go-offline -DskipTests

# 3. Kaynak kodlar
COPY user-service/user-service-client/src user-service/user-service-client/src
COPY shop-service/shop-service-client/src shop-service/shop-service-client/src
COPY warehouse-service/warehouse-service-app/src warehouse-service/warehouse-service-app/src
COPY warehouse-service/warehouse-service-client/src warehouse-service/warehouse-service-client/src

#bagimli oldugu modullerin client kodlari
RUN --mount=type=cache,target=/root/.m2 \
    mvn -pl user-service/user-service-client,shop-service/shop-service-client,warehouse-service/warehouse-service-client \
    clean install -DskipTests

# 4. Build
RUN --mount=type=cache,target=/root/.m2 \
    mvn -pl warehouse-service/warehouse-service-app -am clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/warehouse-service/warehouse-service-app/target/warehouse-service-app-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
