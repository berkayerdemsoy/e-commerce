## 1. Build aşaması
#FROM maven:3.9.9-eclipse-temurin-21 AS build
#
#WORKDIR /app
#
## Tüm projeyi kopyala (çünkü parent pom ve diğer modüller lazım)
#COPY . .
#
## Sadece user-service-app ve bağımlı modüllerini build et
#RUN mvn -pl user-service/user-service-app -am clean package -DskipTests
#
## 2. Run aşaması
#FROM eclipse-temurin:21-jre-alpine
#WORKDIR /app
#
## Sadece app modülünün jar'ını al
#COPY --from=build /app/user-service/user-service-app/target/user-service-app-0.0.1-SNAPSHOT.jar app.jar
#
#EXPOSE 8080
#ENTRYPOINT ["java","-jar","app.jar"]
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

# 1. ADIM: Önce POM dosyalarını kopyala (bağımlılıklar değişmedikçe bu katman cache'lenir)
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

# 2. ADIM: Bağımlılıkları indir (bu katman cache'lenir, kod değişse bile bağımlılıklar değişmediyse tekrar inmez)
RUN --mount=type=cache,target=/root/.m2 \
    mvn -pl user-service/user-service-app -am dependency:go-offline -DskipTests

# 3. ADIM: Kaynak kodları kopyala (kod değişince sadece bu katman yeniden çalışır)
COPY common/src common/src
COPY user-service/user-service-app/src user-service/user-service-app/src
COPY user-service/user-service-client/src user-service/user-service-client/src

RUN --mount=type=cache,target=/root/.m2 \
    mvn -pl user-service/user-service-client \
    clean install -DskipTests

# 4. ADIM: Build et
RUN --mount=type=cache,target=/root/.m2 \
    mvn -pl user-service/user-service-app -am clean package -DskipTests

# Runtime aşaması
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/user-service/user-service-app/target/user-service-app-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
