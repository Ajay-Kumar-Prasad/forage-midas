# Midas - Transaction Processing System

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.5-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Apache Kafka](https://img.shields.io/badge/Apache%20Kafka-3.1.4-black.svg)](https://kafka.apache.org/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

A distributed transaction-processing system built with Spring Boot, Apache Kafka, and H2 database. Developed as part of the **JPMorgan Chase Advanced Software Engineering – Forage Program**.

## 🎯 Project Overview

Midas is a scalable backend system that processes financial transactions using event-driven architecture. The system integrates message queuing (Kafka), persistent storage (H2), and external APIs to handle transaction validation, processing, and balance management.

### Key Features

- **Event-Driven Architecture**: Kafka-based message queue for decoupled, asynchronous transaction processing
- **Transaction Validation**: Real-time validation of sender/recipient IDs and account balances
- **Persistent Storage**: JPA-based H2 database integration for transaction records and user balances
- **External API Integration**: Incentive system integration via REST API
- **Balance Query API**: RESTful endpoint for querying user account balances

## 🏗️ Architecture

```
┌─────────────┐         ┌──────────────┐         ┌─────────────┐
│  Frontend   │────────▶│    Kafka     │────────▶│  Midas Core │
│  Producer   │         │    Queue     │         │   Consumer  │
└─────────────┘         └──────────────┘         └──────┬──────┘
                                                         │
                                    ┌────────────────────┼────────────────┐
                                    │                    │                │
                              ┌─────▼─────┐      ┌──────▼──────┐  ┌─────▼──────┐
                              │    H2     │      │  Incentive  │  │  Balance   │
                              │ Database  │      │     API     │  │    API     │
                              └───────────┘      └─────────────┘  └────────────┘
```

## 🚀 Getting Started

### Prerequisites

- **Java 17** or higher
- **Maven 3.6+**
- **Git**
- IDE with Spring Boot support (recommended: IntelliJ IDEA Community Edition)

### Installation

1. **Fork and Clone the Repository**

```bash
git clone https://github.com/your-username/forage-midas.git
cd forage-midas
```

2. **Verify Java Installation**

```bash
java -version
# Should display Java 17 or higher
```

3. **Install Dependencies**

```bash
mvn clean install
```

4. **Run the Application**

```bash
mvn spring-boot:run
```

The application will start on port `33400`.

## 📋 Project Tasks

### Task 1: Project Setup ✅

**Objective**: Configure the development environment and validate setup with automated tests.

**Key Activities**:
- Set up Java 17 and Maven
- Configure Spring Boot dependencies
- Add Kafka and H2 database dependencies
- Run validation tests

**Dependencies Added**:
| Dependency | Group | Version |
|-----------|-------|---------|
| spring-boot-starter-data-jpa | org.springframework.boot | 3.2.5 |
| spring-boot-starter-web | org.springframework.boot | 3.2.5 |
| spring-kafka | org.springframework.kafka | 3.1.4 |
| h2 | com.h2database | 2.2.224 |
| spring-boot-starter-test | org.springframework.boot | 3.2.5 |
| spring-kafka-test | org.springframework.kafka | 3.1.4 |
| kafka (Testcontainers) | org.testcontainers | 1.19.1 |

---

### Task 2: Kafka Integration ✅

**Objective**: Implement a Kafka message consumer to process incoming transactions.

**Key Activities**:
- Create Kafka listener for the `trader-updates` topic
- Deserialize JSON messages into `Transaction` objects
- Verify integration using embedded Kafka and automated tests

**Sample Transaction Messages**:
```json
{
  "senderId": 6,
  "recipientId": 7,
  "amount": 122.86
}
```

**Benefits of Message Queue Architecture**:
- **Decoupling**: Frontend and backend operate independently
- **Asynchronous Communication**: Backend processes transactions at its own pace
- **Scalability**: Support for multiple producers and consumers

---

### Task 3: Database Integration ✅

**Objective**: Integrate H2 database for transaction persistence and user balance management.

**Key Activities**:
- Configure Spring Data JPA with H2 in-memory database
- Implement `TransactionRecord` entity with JPA annotations
- Create transaction validation logic:
  - Validate sender and recipient IDs
  - Check sufficient sender balance
  - Update balances for valid transactions
- Establish many-to-one relationships between transactions and users

**Validation Rules**:
- ✅ Sender ID exists
- ✅ Recipient ID exists
- ✅ Sender balance ≥ transaction amount

**Database Schema**:
```
User (id, username, balance)
TransactionRecord (id, senderId, recipientId, amount, timestamp)
```

---

### Task 4: External API Integration ✅

**Objective**: Connect to Incentive API to process transaction bonuses.

**Key Activities**:
- Implement REST client to POST transactions to `/incentive` endpoint
- Process `Incentive` response objects
- Update balance calculation logic:
  - Add incentive amount to recipient balance
  - Do not deduct incentive from sender balance

**Flow**:
```
Transaction → Validate → Post to Incentive API → Receive Bonus → Update Balances
```

---

### Task 5: REST API Development ✅

**Objective**: Expose a RESTful API endpoint for querying user balances.

**Key Activities**:
- Create Spring REST controller
- Implement `GET /balance` endpoint
- Accept `userId` as request parameter
- Return JSON-serialized `Balance` object
- Handle non-existent users (return balance of 0)

**API Specification**:
```http
GET http://localhost:33400/balance?userId=123

Response:
{
  "amount": 1234.56
}
```

## 🧪 Testing

Run all tests:
```bash
mvn test
```

Run specific task tests:
```bash
mvn -Dtest=TaskOneTests test
mvn -Dtest=TaskTwoTests test
mvn -Dtest=TaskThreeTests test
mvn -Dtest=TaskFourTests test
mvn -Dtest=TaskFiveTests test
```

## 📁 Project Structure

```
forage-midas/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/jpmc/midas/
│   │   │       ├── config/          # Configuration classes
│   │   │       ├── controller/      # REST controllers
│   │   │       ├── entity/          # JPA entities
│   │   │       ├── listener/        # Kafka listeners
│   │   │       ├── model/           # Domain models
│   │   │       ├── repository/      # Data repositories
│   │   │       └── service/         # Business logic
│   │   └── resources/
│   │       └── application.yml      # Application configuration
│   └── test/
│       └── java/                    # Test classes
├── pom.xml                          # Maven configuration
└── README.md
```

## 🛠️ Technologies Used

- **Java 17**: Core programming language
- **Spring Boot 3.2.5**: Application framework
- **Apache Kafka 3.1.4**: Message queue system
- **H2 Database 2.2.224**: In-memory SQL database
- **Spring Data JPA**: Object-relational mapping
- **Maven**: Dependency management and build tool
- **Testcontainers**: Integration testing with Kafka

## 🔧 Configuration

Key configuration in `application.yml`:

```yaml
general:
  kafka-topic: trader-updates

server:
  port: 33400

spring:
  datasource:
    url: jdbc:h2:mem:midasdb
    driver-class-name: org.h2.Driver
  jpa:
    hibernate:
      ddl-auto: update
```

## 📊 Performance Considerations

- **Asynchronous Processing**: Kafka enables non-blocking transaction handling
- **In-Memory Database**: H2 provides fast read/write operations for development
- **Connection Pooling**: Spring Boot default HikariCP for optimized database connections
- **Horizontal Scalability**: Multiple Kafka consumers can process transactions in parallel

## 🎓 Learning Outcomes

Through this project, I gained hands-on experience with:

- **Distributed Systems**: Building scalable, decoupled architectures using message queues
- **Event-Driven Design**: Processing asynchronous events with Apache Kafka
- **Spring Boot Ecosystem**: JPA, REST controllers, and Kafka integration
- **Database Management**: Entity modeling, relationships, and transaction processing
- **API Integration**: Consuming and exposing RESTful web services
- **Enterprise Patterns**: Implementing production-grade validation and error handling

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- **JPMorgan Chase** for the Advanced Software Engineering program
- **Forage** for the educational platform and resources
- **Apache Kafka** and **Spring** communities for excellent documentation

## 📧 Contact

For questions or feedback about this project, please open an issue in the repository.

---

**Note**: This project was completed as part of the JPMorgan Chase Advanced Software Engineering virtual experience program on Forage. It demonstrates practical backend development skills including microservices architecture, event-driven systems, and RESTful API design.