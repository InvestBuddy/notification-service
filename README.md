# Notification Service - InvestBuddy AI

The **Notification Service** is responsible for sending notifications and alerts to users in the **InvestBuddy AI** platform. It listens to events from other microservices via **Kafka** and delivers messages via email, SMS, or push notifications.

---

## 📜 Table of Contents

- [Features](#-features)
- [Architecture](#-architecture)
- [Prerequisites](#-prerequisites)
- [Installation](#-installation)
- [Configuration](#-configuration)
---

## ✨ Features

- **Real-time Notifications**: Processes Kafka events to send real-time notifications.
- **Multi-channel Delivery**:
    - Email notifications
    - Push notifications
- **Event-driven Design**: Consumes messages from Kafka topics such as:
    - `user-created`: Send email verification for new users.
    - `kyc-verification`: Notifications about KYC status updates.
- **Template-based Messaging**: Uses pre-defined templates for consistent communication.
- **Integration with Discovery Server**: Registers itself for service discovery.

---

## 🏗️ Architecture

The **Notification Service** is part of the **InvestBuddy AI** ecosystem and uses an event-driven architecture with **Kafka**. Key technologies include:

- **Spring Boot**: Backend framework for REST API and business logic.
- **Kafka**: Message broker for event-driven communication.
- **MailGun**: API for sending email notifications.
- **Spring Cloud**: Integration with Eureka for service discovery.

---

## ✅ Prerequisites

Ensure the following are installed before setting up the **Notification Service**:

- **Java 21** or higher
- **Maven 3.8** or higher
- **Kafka** (running instance or cluster)
- **SMTP Server** (e.g., Gmail SMTP for email notifications)
- **Discovery Server** (Eureka)

---

## 🛠️ Installation

1. Clone this repository:

   ```bash
   git clone https://github.com/your-repo/notification-service.git
   cd notification-service
