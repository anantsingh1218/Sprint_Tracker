# Sprint Tracker

A comprehensive, Jira-like sprint tracking and project management tool designed to streamline agile workflows. This application provides a robust backend API built with Spring Boot and a dynamic, responsive frontend built with Angular.

## Project Structure

This repository is divided into two main components:

- **Frontend**: The user interface, built using Angular 21, Angular Material, and Chart.js.
- **Backend**: The RESTful API and core business logic, built using Java 25, Spring Boot 4.0.7, and PostgreSQL.

## Technologies Used

### Frontend
- **Framework**: Angular
- **UI Components**: Angular Material
- **Data Visualization**: Chart.js
- **Styling**: SCSS / CSS

### Backend
- **Framework**: Spring Boot
- **Language**: Java
- **Database**: PostgreSQL (via Spring Data JPA)
- **Security**: Spring Security with JWT Authentication
- **Caching**: Caffeine
- **Observability**: OpenTelemetry
- **Build Tool**: Maven

## Getting Started

### Prerequisites
- Node.js
- npm (Node Package Manager)
- Java Development Kit (JDK) 25
- PostgreSQL Database
- Docker (optional, for running dependencies via compose)

### Frontend Setup

1. Navigate to the frontend directory:
   ```bash
   cd Frontend
   ```
2. Install dependencies:
   ```bash
   npm install
   ```
3. Start the development server:
   ```bash
   npm start
   ```
4. Access the application at `http://localhost:4200/`.

### Backend Setup

1. Navigate to the backend directory:
   ```bash
   cd Backend/SprintBackend
   ```
2. Ensure your PostgreSQL database is running or start it using the provided `compose.yaml`.
3. Build and run the application using the Maven wrapper:
   ```bash
   ./mvnw spring-boot:run
   ```
4. The backend server will start and connect to the configured database.

## Features
- User authentication and authorization via JWT.
- Sprint planning, creation, and management.
- Task tracking and status updates.
- Performance and progress visualization using interactive charts.
- Secure, scalable, and observable architecture.

## License
This project is proprietary and confidential.
