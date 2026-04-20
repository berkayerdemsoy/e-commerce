# MicroMart

<p align="center">
  <a href="https://readme-typing-svg.demolab.com">
    <img src="https://readme-typing-svg.demolab.com?font=Fira+Code&weight=600&size=22&pause=1000&color=4A90D9&center=true&vCenter=true&width=700&lines=Cloud-Native+E-Commerce+Platform;Microservices+%7C+DDD+%7C+Event-Driven+Architecture;Java+21+%7C+Spring+Boot+3.2+%7C+Apache+Kafka;Designed+for+Scale+%26+Resilience;8+Independent+Services+%7C+12%2B+Containers" alt="Typing SVG" />
  </a>
</p>

<p align="center">
  <strong>Cloud-Native E-Commerce &amp; Warehouse Management Platform</strong>
</p>

MicroMart is a production-grade, end-to-end e-commerce system built on a microservices architecture with Domain-Driven Design (DDD) and Event-Driven principles. The platform handles the full commercial lifecycle — product catalog, cart management, order orchestration, payment processing, warehouse operations, and shipment tracking — across independent, loosely coupled services.

Designed as a direct response to the scaling and maintenance limitations of monolithic systems, MicroMart applies **Loose Coupling** and **High Cohesion** throughout its domain boundaries.

---

## Architecture Overview

The system is decomposed into eight bounded contexts, each deployed as an independent service. All external traffic enters through a single API Gateway, while inter-service communication uses both synchronous (OpenFeign/REST) and asynchronous (Kafka) channels depending on consistency requirements.

```
┌─────────────────────────────────────────────────────────────────────┐
│                            CLIENT                                   │
└─────────────────────────────┬───────────────────────────────────────┘
                              │ HTTP
                              ▼
                    ┌─────────────────┐
                    │   API Gateway   │  :8080
                    │ Spring Cloud GW │
                    └────────┬────────┘
                             │ Routes
          ┌──────────────────┼──────────────────────┐
          │                  │                      │
          ▼                  ▼                      ▼
  ┌───────────────┐  ┌───────────────┐   ┌──────────────────┐
  │  User Service │  │  Shop Service │   │  Cart Service    │
  │    :8081      │  │    :8082      │   │    :8084         │
  │  [user-db]    │  │  [shop-db]    │   │    [Redis]       │
  └───────────────┘  └───────────────┘   └────────┬─────────┘
                                                   │ OpenFeign
          ┌────────────────────────────────────────▼─────────┐
          │                  Order Service  :8085             │
          │              [order-db]  [Keycloak OAuth2]        │
          └────────────┬──────────────────────────────────────┘
                       │
              Kafka Events
                       │
          ┌────────────┴─────────────────────────────────────┐
          │                                                   │
          ▼                                                   ▼
  ┌───────────────────┐                         ┌────────────────────┐
  │  Payment Service  │                         │ Warehouse Service  │
  │      :8086        │                         │      :8083         │
  │  [payment-db]     │                         │  [warehouse-db]    │
  └───────────────────┘                         └────────────────────┘
                                                         │
                                                  Kafka Events
                                                         │
                                               ┌─────────▼──────────┐
                                               │ Shipment Service   │
                                               │      :8087         │
                                               │  [shipment-db]     │
                                               └────────────────────┘
```

---

## Order Lifecycle Flow

The following sequence illustrates how a purchase flows through the system asynchronously once an order is placed.

```
Client          API Gateway      Order Service     Kafka Broker
  │                  │                │                  │
  │── POST /orders ─►│                │                  │
  │                  │── forward ────►│                  │
  │                  │                │── ORDER_CREATED ►│
  │                  │                │                  │
  │                  │                │          ┌───────┴────────┐
  │                  │                │          │                │
  │                  │                │    Payment Svc       Warehouse Svc
  │                  │                │     processes            reserves
  │                  │                │     payment              stock
  │                  │                │          │                │
  │                  │                │◄─ PAYMENT_SUCCESS ────────┘
  │                  │                │
  │                  │                │── SHIPMENT_INITIATED ►│
  │                  │                │                       │
  │                  │                │                 Shipment Svc
  │                  │                │                  creates
  │                  │                │                  tracking
  │◄─ 201 Created ───│◄───────────────│
```

---

## Design Principles

| Principle | Implementation |
|---|---|
| Database per Service | Each service owns an isolated PostgreSQL instance; no shared schema |
| Event-Driven Architecture | Order, payment, and shipment flows are decoupled via Apache Kafka topics |
| Distributed Caching | Cart state is stored entirely in Redis for sub-millisecond read/write latency |
| Centralized Security | All authentication and authorization delegated to Keycloak (OAuth2 / OIDC / JWT) |
| Optimistic Locking | Warehouse service uses optimistic locking to prevent overselling under concurrent load |
| Idempotent Payments | Payment service guards against duplicate charges using idempotency keys |
| Strategy Pattern | Shipment service selects a carrier at runtime via the Strategy design pattern |
| Infrastructure as Code | The full stack — 12+ containers — is defined and launched with a single `docker-compose up` |

---

## Technology Stack

**Backend & Framework**

![Java](https://img.shields.io/badge/Java_21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot_3.2-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white)
![Spring Cloud](https://img.shields.io/badge/Spring_Cloud_Gateway-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![OpenFeign](https://img.shields.io/badge/OpenFeign-6DB33F?style=for-the-badge&logo=spring&logoColor=white)

**Messaging & Caching**

![Apache Kafka](https://img.shields.io/badge/Apache_Kafka-231F20?style=for-the-badge&logo=apachekafka&logoColor=white)
![Redis](https://img.shields.io/badge/Redis_7-DC382D?style=for-the-badge&logo=redis&logoColor=white)
![Zookeeper](https://img.shields.io/badge/Zookeeper-E95420?style=for-the-badge&logo=apache&logoColor=white)

**Persistence**

![PostgreSQL](https://img.shields.io/badge/PostgreSQL_15-4169E1?style=for-the-badge&logo=postgresql&logoColor=white)
![Liquibase](https://img.shields.io/badge/Liquibase-2962FF?style=for-the-badge&logo=liquibase&logoColor=white)

**Security**

![Keycloak](https://img.shields.io/badge/Keycloak_21-4D4D4D?style=for-the-badge&logo=keycloak&logoColor=white)
![OAuth2](https://img.shields.io/badge/OAuth2-EB5424?style=for-the-badge&logo=auth0&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white)

**Infrastructure & DevOps**

![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![Docker Compose](https://img.shields.io/badge/Docker_Compose-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)
![Git](https://img.shields.io/badge/Git-F05032?style=for-the-badge&logo=git&logoColor=white)

---

## Service Catalog

| Service | Port | Responsibility |
|---|---|---|
| API Gateway | 8080 | Single ingress point. Handles routing, JWT validation, and rate limiting |
| User Service | 8081 | User profile and role management integrated with Keycloak |
| Shop Service | 8082 | Product catalog, categories, and pricing (read-optimised) |
| Warehouse Service | 8083 | Stock, shelf, and aisle management with optimistic concurrency control |
| Cart Service | 8084 | Redis-backed cart with high-throughput I/O |
| Order Service | 8085 | Full order lifecycle and state-machine orchestration |
| Payment Service | 8086 | Payment simulation with idempotency and duplicate-charge prevention |
| Shipment Service | 8087 | Carrier selection and delivery tracking via Strategy pattern |

---

## Getting Started

**Prerequisites:** Docker and Docker Compose must be installed locally.

**Step 1 — Clone the repository**

```bash
git clone https://github.com/berkayerdemsoy/e-commerce.git
cd e-commerce
```

**Step 2 — Configure environment variables**

```bash
cp backend/.env.example backend/.env
```

Open `backend/.env` and replace `ORDER_SERVICE_CLIENT_SECRET` with the client secret generated in the Keycloak admin console.

**Step 3 — Build all service JARs**

```bash
cd backend
./mvnw clean package -DskipTests
```

**Step 4 — Start the full stack**

```bash
docker-compose up -d --build
```

This command starts all infrastructure components and application services (~12 containers) in dependency order.

---

## Access Points

| Interface | URL | Credentials |
|---|---|---|
| API Gateway | http://localhost:8080 | — |
| Keycloak Admin Console | http://localhost:8180 | admin / admin |
| Kafka UI | http://localhost:8090 | — |
| PgAdmin | http://localhost:8888 | Configured via `PGADMIN_DEFAULT_EMAIL` / `PGADMIN_DEFAULT_PASSWORD` in `.env` |

---

## Roadmap

- [ ] Kubernetes migration with Helm chart definitions
- [ ] Centralised log aggregation with the ELK Stack (Elasticsearch, Logstash, Kibana)
- [ ] Observability layer with Prometheus metrics and Grafana dashboards
- [ ] Distributed tracing with OpenTelemetry
- [ ] Kubernetes-native service discovery replacing manual configuration

---

## Author

**Berkay Erdemsoy**

[LinkedIn](https://www.linkedin.com/in/berkay-erdemsoy) · berkayerdemsoy@gmail.com

> Developed as part of the HAVELSAN A.Ş. Workplace Training Programme.
