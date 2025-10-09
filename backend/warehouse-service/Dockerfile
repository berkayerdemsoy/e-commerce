FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app

# mvnw ve pom önce: bağımlılıklar cache'lenir
COPY pom.xml .
COPY mvnw .
COPY .mvn ./.mvn
RUN chmod +x ./mvnw

# BuildKit cache kullanarak bağımlılıkları indir
RUN --mount=type=cache,target=/root/.m2 \
    ./mvnw -B dependency:go-offline

# Kaynakları sonra kopyala
COPY src ./src

# Paketle (testleri atlamak isterseniz -DskipTests kullanın)
RUN --mount=type=cache,target=/root/.m2 \
    ./mvnw -B -DskipTests package

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]

# $env:DOCKER_BUILDKIT = "1"; docker compose up --build