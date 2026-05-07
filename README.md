# Smart Clinic Management System - Architecture Design

## Section 1: Architecture Summary

The Smart Clinic Management System is a comprehensive Spring Boot application that combines multiple architectural patterns to serve diverse client needs. The system implements both server-side rendered views and RESTful APIs to accommodate different user interactions and integration patterns.

The application is structured around a three-tier architecture that ensures clean separation of concerns and promotes maintainability. The presentation tier includes Thymeleaf-based dashboards for administrative and clinical staff, while REST APIs handle appointment scheduling, patient dashboards, and patient records management. The application tier is powered by Spring Boot, which orchestrates request handling through specialized controllers and centralizes all business logic within a shared service layer. The data tier leverages a dual-database strategy: MySQL stores normalized, structured data for core entities (patients, doctors, appointments, and admin records) while MongoDB manages flexible, document-based prescriptions that evolve rapidly in structure.

This architecture maximizes scalability and extensibility by allowing independent development and deployment of MVC and REST modules. The service layer acts as the single source of truth for business logic, ensuring consistency across all client types. By abstracting database access through repositories, the system maintains flexibility for future database migrations or additions without impacting controller or service logic.

---

## Section 2: Numbered Flow of Data and Control

The following steps trace how requests flow through the Smart Clinic Management System from user interaction to data persistence and retrieval:

1. **User Interface Access** – Users interact with the application through two distinct interfaces. Administrators and doctors access server-rendered dashboards (AdminDashboard and DoctorDashboard), while other users interact with RESTful modules (Appointments, PatientDashboard, and PatientRecord) via JSON-based APIs. Each interface type serves different consumption patterns and client architectures.

2. **Controller Routing** – Incoming requests are routed to appropriate controllers based on HTTP methods and URL paths. Thymeleaf Controllers handle requests for server-rendered views from dashboard users, returning HTML pages populated with dynamic data. REST Controllers handle API requests from client applications, accepting JSON payloads and returning structured data responses.

3. **Service Layer Delegation** – Both Thymeleaf and REST controllers delegate all business logic to the Service Layer. This layer applies business rules, validates inputs, orchestrates complex workflows (such as checking doctor availability before scheduling appointments), and ensures that all business decisions are made in a single, testable location.

4. **Repository Selection** – The Service Layer determines whether to access MySQL or MongoDB based on the data being requested. For patient, doctor, appointment, and admin information, it calls MySQL Repositories. For prescription data, it calls the MongoDB Repository.

5. **Database Access** – MySQL Repositories use Spring Data JPA to execute queries against the relational MySQL database, while the MongoDB Repository uses Spring Data MongoDB to execute queries against the document-oriented MongoDB database. Each repository type is optimized for its respective database engine and data model.

6. **Model Binding (Relational)** – Data retrieved from MySQL is automatically mapped into JPA Entity objects (Patient, Doctor, Appointment, Admin) by the Spring Data framework. These entities represent normalized rows from relational tables and are fully managed by the JPA persistence layer, including change tracking and lazy loading.

7. **Response Formatting** – The bound models are returned through the appropriate response pathway. In MVC flows, models are passed to Thymeleaf templates where they are rendered as dynamic HTML pages for browser delivery. In REST flows, the same models (or transformed DTOs) are serialized into JSON and returned as HTTP responses to API clients. MongoDB prescriptions are similarly bound to MongoDB Document models and serialized into JSON for API responses.

---

## Architecture Key Points

- **Separation of Concerns:** Each layer (presentation, application, data) has distinct responsibilities and can be modified independently.
- **Reusable Business Logic:** The centralized service layer prevents duplication and ensures consistent business rule enforcement across all client types.
- **Dual-Database Flexibility:** MySQL handles transactional, normalized data while MongoDB accommodates flexible, evolving document structures.
- **API-First Design:** REST endpoints enable future integrations with mobile applications, third-party services, and modern frontend frameworks.
- **Cloud-Ready Deployment:** Spring Boot's containerization support and stateless design enable seamless deployment to Kubernetes, Docker, and cloud platforms.
