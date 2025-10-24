FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

# 1. ADIM: Önce parent ve modül pom.xml dosyalarını kopyala
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
COPY payment-service/payment-service-client/pom.xml payment-service/payment-service-client/
COPY payment-service/payment-service-app/pom.xml payment-service/payment-service-app/

# 2. ADIM: Bağımlılıkları indir (cache kullanılır)
RUN --mount=type=cache,target=/root/.m2 \
    mvn -pl cart-service/cart-service-app -am dependency:go-offline -DskipTests

# 3. ADIM: Kaynak kodları kopyala
COPY common/src common/src
COPY shop-service/shop-service-client/src shop-service/shop-service-client/src
COPY cart-service/cart-service-client/src cart-service/cart-service-client/src
COPY cart-service/cart-service-app/src cart-service/cart-service-app/src

# 4. ADIM: Önce client build et
RUN --mount=type=cache,target=/root/.m2 \
    mvn -pl shop-service/shop-service-client,cart-service/cart-service-client clean install -DskipTests

# 5. ADIM: App build et
RUN --mount=type=cache,target=/root/.m2 \
    mvn -pl cart-service/cart-service-app -am clean package -DskipTests

# Runtime aşaması
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/cart-service/cart-service-app/target/cart-service-app-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
