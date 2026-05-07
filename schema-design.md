# Schema Design: Smart Clinic Management System

## Overview

This system uses a hybrid data storage approach:
- **MySQL** (via Spring Data JPA) for structured, relational data: users, doctors, patients, and appointments.
- **MongoDB** (via Spring Data MongoDB) for flexible, document-based data: prescriptions and appointment notes.

---

## MySQL Database Design

### Table: `admin`

Stores admin credentials and profile information.

| Column       | Data Type     | Constraints                  |
|--------------|---------------|------------------------------|
| id           | BIGINT        | PRIMARY KEY, AUTO_INCREMENT  |
| username     | VARCHAR(255)  | NOT NULL                     |
| password     | VARCHAR(255)  | NOT NULL                     |

---

### Table: `doctors`

Stores doctor profiles and credentials.

| Column         | Data Type     | Constraints                        |
|----------------|---------------|------------------------------------|
| id             | BIGINT        | PRIMARY KEY, AUTO_INCREMENT        |
| name           | VARCHAR(100)  | NOT NULL                           |
| specialty      | VARCHAR(50)   | NOT NULL                           |
| email          | VARCHAR(150)  | NOT NULL, UNIQUE                   |
| password       | VARCHAR(255)  | NOT NULL                           |
| phone          | CHAR(10)      | NOT NULL, must match `^[0-9]{10}$` |

`available_times` is stored in a separate `doctor_available_times` table via `@ElementCollection`:

| Column          | Data Type    | Constraints                              |
|-----------------|--------------|------------------------------------------|
| doctor_id       | BIGINT       | FOREIGN KEY REFERENCES doctors(id)       |
| available_times | VARCHAR(50)  | e.g. "09:00-10:00"                       |

---

### Table: `patients`

Stores patient profiles and login credentials.

| Column    | Data Type    | Constraints                        |
|-----------|--------------|------------------------------------|
| id        | BIGINT       | PRIMARY KEY, AUTO_INCREMENT        |
| name      | VARCHAR(100) | NOT NULL                           |
| email     | VARCHAR(150) | NOT NULL, UNIQUE                   |
| password  | VARCHAR(255) | NOT NULL                           |
| phone     | CHAR(10)     | NOT NULL, must match `^[0-9]{10}$` |
| address   | VARCHAR(255) | NOT NULL                           |

---

### Table: `appointments`

Links patients and doctors; tracks booking status and scheduling.

| Column           | Data Type     | Constraints                                                     |
|------------------|---------------|-----------------------------------------------------------------|
| id               | BIGINT        | PRIMARY KEY, AUTO_INCREMENT                                     |
| doctor_id        | BIGINT        | NOT NULL, FOREIGN KEY REFERENCES doctors(id)                    |
| patient_id       | BIGINT        | NOT NULL, FOREIGN KEY REFERENCES patients(id)                   |
| appointment_time | DATETIME      | NOT NULL, must be a future date/time                            |
| status           | INT           | NOT NULL — 0 = Scheduled, 1 = Completed                        |

`getEndTime()`, `getAppointmentDate()`, and `getAppointmentTimeOnly()` are computed fields (`@Transient`) and are not persisted.

---

## MongoDB Collection Design

### Collection: `prescriptions`

Prescriptions are document-based because their structure varies per appointment — medication names, dosage, and doctor notes are variable in length and format, making a rigid relational schema impractical.

Each document is linked to a MySQL `appointments` record via `appointmentId`.

**Example Document:**

```json
{
  "_id": { "$oid": "664f2a3b1c9d4e0012a3b456" },
  "appointmentId": 42,
  "patientName": "Jane Doe",
  "medication": "Amoxicillin",
  "dosage": "500mg — 3 times daily for 7 days",
  "doctorNotes": "Take after meals. Review if symptoms persist beyond 5 days."
}
```

**Design decisions:**
- `appointmentId` is a reference to the MySQL `appointments` table so the REST layer can join prescription data with appointment context.
- `doctorNotes` is kept flexible (free-text, max 200 chars) since note content varies widely between appointments.
- MongoDB is chosen here over a relational table because prescriptions don't need to participate in joins and benefit from schema flexibility as medication data evolves.

---

## Relationship Diagram (summary)

```
admin          (MySQL)
doctors        (MySQL)  ──┐
                           ├── appointments (MySQL)
patients       (MySQL)  ──┘        │
                                   │ appointmentId (reference)
                            prescriptions (MongoDB)
```