MicroMart: Cloud-Native E-Commerce & Warehouse Management System
📖 Proje Hakkında
MicroMart, modern yazılım mühendisliği prensipleri (Microservices, DDD, Event-Driven Architecture) kullanılarak geliştirilmiş, uçtan uca bir E-Ticaret ve Depo Yönetim Sistemidir.

Proje sadece bir alışveriş arayüzü sunmakla kalmaz; arka planda sipariş orkestrasyonu, stok tutarlılığı, finansal simülasyonlar ve lojistik süreçlerini dağıtık bir mimaride yönetir. Monolitik yapıların ölçeklenme ve bakım sorunlarına çözüm olarak "Loose Coupling" (Gevşek Bağlılık) ve "High Cohesion" (Yüksek Bütünlük) prensipleriyle tasarlanmıştır.

🏗️ Mimari Tasarım
Sistem, Domain Driven Design (DDD) yaklaşımıyla iş alanlarına (Bounded Contexts) bölünmüş bağımsız mikroservislerden oluşur.

🚀 Temel Özellikler
Event-Driven Architecture: Sipariş ve stok süreçleri Apache Kafka üzerinden asenkron olarak haberleşir.

Distributed Caching: Sepet (Cart) işlemleri Redis üzerinde tutularak milisaniyeler seviyesinde yanıt süresi sağlanır.

Centralized Security: Tüm kimlik doğrulama işlemleri Keycloak (OAuth2 & OIDC) üzerinden yönetilir.

Infrastructure as Code: Tüm altyapı (DBs, Kafka, Zookeeper, Apps) Docker Compose ile tek komutla ayağa kalkar.

Database per Service: Her servis kendi izole PostgreSQL veritabanına sahiptir.

🛠️ Teknoloji Yığını (Tech Stack)
| Kategori | Teknolojiler |

|Data Persistence| PostgreSQL, Liquibase (Migration)| 

|Backend Framework| Java 17, Spring Boot 3.x, Spring Data JPA| 

|Microservices Config| Spring Cloud Gateway, OpenFeign| 

|Messaging & Events| Apache Kafka, Zookeeper| 

|Caching & Performance| Redis (In-Memory Data Structure Store)| 

|Security| Keycloak, OAuth2, JWT, Spring Security| 

|DevOps & Tools| Docker, Docker Compose, Maven, Git|

API Gateway	       8080	      Tek giriş noktası. Routing, SSL Termination ve Rate Limiting yapar.
User Service	     8081	      Keycloak entegrasyonu ile kullanıcı profil ve rol yönetimi.
Shop Service	     8082	      Ürün kataloğu, kategoriler ve fiyat bilgileri (Read-Heavy).
Warehouse Service	 8083	      Stok, raf ve koridor yönetimi. Optimistic Locking ile veri tutarlılığı.
Cart Service	     8084	      Redis tabanlı sepet yönetimi. Yüksek I/O performansı.
Order Service	     8085       Sipariş yaşam döngüsü ve durum makinesi (State Machine).
Payment Service	   8086       Ödeme simülasyonu. Idempotency ile mükerrer ödeme koruması.
Shipment Service	 8087	      Kargo ve teslimat süreçleri. Strategy Pattern ile kargo firması yönetimi.


⚙️ Kurulum ve Çalıştırma
Projeyi yerel ortamınızda çalıştırmak için Docker ve Docker Compose yüklü olmalıdır.

Adım 1: Projeyi Klonlayın

git clone https://github.com/berkayerdemsoy/e-commerce.git
cd e-commerce

Adım 2: JAR Dosyalarını Oluşturun (Maven)

Proje kök dizininde:
./mvnw clean package -DskipTests

Adım 3: Docker Konteynerlerini Başlatın

docker-compose up -d --build

Bu işlem; Kafka, Zookeeper, Redis, Postgres veritabanları, Keycloak ve tüm mikroservisleri (Toplam ~12 konteyner) doğru sırayla ayağa kaldıracaktır.

Adım 4: Erişim
API Gateway: http://localhost:8080

Keycloak Admin Console: http://localhost:8180 (User: admin, Pass: admin)

Kafka UI: http://localhost:8090

PgAdmin: http://localhost:8888

🔮 Gelecek Planları (Roadmap)
[ ] Kubernetes (K8s) ortamına geçiş ve Helm chart'larının hazırlanması.

[ ] ELK Stack (Elasticsearch, Logstash, Kibana) ile merkezi log yönetimi.

[ ] Prometheus & Grafana ile metrik takibi ve alarm mekanizmaları.

[ ] Netflix Eureka yerine Kubernetes Service Discovery entegrasyonu.

👨‍💻 İletişim
Geliştirici: BERKAY ERDEMSOY LinkedIn: www.linkedin.com/in/berkay-erdemsoy Email: berkayerdemsoy@gmail.com

Bu proje HAVELSAN A.Ş. İşyeri Eğitimi kapsamında geliştirilmiştir.
