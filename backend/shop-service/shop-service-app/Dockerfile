#FROM maven:3.9.9-eclipse-temurin-21 AS build
#
#WORKDIR /app
#
## Tüm projeyi kopyala (çünkü parent pom ve diğer modüller lazım)
#COPY . .
#
## Sadece user-service-app ve bağımlı modüllerini build et
#RUN mvn -pl shop-service/shop-service-app -am clean package -DskipTests
#
## 2. Run aşaması
#FROM eclipse-temurin:21-jre-alpine
#WORKDIR /app
#
## Sadece app modülünün jar'ını al
#COPY --from=build /app/shop-service/shop-service-app/target/shop-service-app-0.0.1-SNAPSHOT.jar app.jar
#
#EXPOSE 8080
#ENTRYPOINT ["java","-jar","app.jar"]
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

# 1. POM dosyaları (DÜZELT: shop-service olmalı)
COPY pom.xml .
COPY common/pom.xml common/
COPY user-service/user-service-app/pom.xml user-service/user-service-app/
COPY user-service/user-service-client/pom.xml user-service/user-service-client/
COPY shop-service/shop-service-app/pom.xml shop-service/shop-service-app/
COPY shop-service/shop-service-client/pom.xml shop-service/shop-service-client/
COPY api-gateway/pom.xml api-gateway/
COPY warehouse-service/warehouse-service-app/pom.xml warehouse-service/warehouse-service-app/
COPY warehouse-service/warehouse-service-client/pom.xml warehouse-service/warehouse-service-client/
COPY cart-service/cart-service-client/pom.xml cart-service/cart-service-client/
COPY cart-service/cart-service-app/pom.xml cart-service/cart-service-app/
COPY order-service/order-service-app/pom.xml order-service/order-service-app/
COPY order-service/order-service-client/pom.xml order-service/order-service-client/

# 2. Bağımlılıkları indir (DÜZELT: shop-service olmalı)
RUN --mount=type=cache,target=/root/.m2 \
    mvn -pl shop-service/shop-service-app -am dependency:go-offline -DskipTests

# 3. Kaynak kodlar (DÜZELT: shop-service olmalı)
COPY common/src common/src
COPY shop-service/shop-service-app/src shop-service/shop-service-app/src
COPY shop-service/shop-service-client/src shop-service/shop-service-client/src

RUN --mount=type=cache,target=/root/.m2 \
    mvn -pl shop-service/shop-service-client \
    clean install -DskipTests

# 4. Build (DÜZELT: shop-service olmalı)
RUN --mount=type=cache,target=/root/.m2 \
    mvn -pl shop-service/shop-service-app -am clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/shop-service/shop-service-app/target/shop-service-app-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
