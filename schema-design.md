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
| name         | VARCHAR(100)  | NOT NULL                     |
| email        | VARCHAR(150)  | NOT NULL, UNIQUE             |
| password     | VARCHAR(255)  | NOT NULL                     |
| created_at   | TIMESTAMP     | DEFAULT CURRENT_TIMESTAMP    |

---

### Table: `doctors`

Stores doctor profiles and credentials.

| Column       | Data Type     | Constraints                        |
|--------------|---------------|------------------------------------|
| id           | BIGINT        | PRIMARY KEY, AUTO_INCREMENT        |
| name         | VARCHAR(100)  | NOT NULL                           |
| email        | VARCHAR(150)  | NOT NULL, UNIQUE                   |
| password     | VARCHAR(255)  | NOT NULL                           |
| specialty    | VARCHAR(100)  | NOT NULL                           |
| phone        | VARCHAR(20)   |                                    |
| available    | BOOLEAN       | NOT NULL, DEFAULT TRUE             |
| created_at   | TIMESTAMP     | DEFAULT CURRENT_TIMESTAMP          |

---

### Table: `patients`

Stores patient profiles and login credentials.

| Column       | Data Type     | Constraints                        |
|--------------|---------------|------------------------------------|
| id           | BIGINT        | PRIMARY KEY, AUTO_INCREMENT        |
| name         | VARCHAR(100)  | NOT NULL                           |
| email        | VARCHAR(150)  | NOT NULL, UNIQUE                   |
| password     | VARCHAR(255)  | NOT NULL                           |
| phone        | VARCHAR(20)   |                                    |
| date_of_birth| DATE          |                                    |
| created_at   | TIMESTAMP     | DEFAULT CURRENT_TIMESTAMP          |

---

### Table: `appointments`

Links patients and doctors; tracks booking status and scheduling.

| Column           | Data Type    | Constraints                                                  |
|------------------|--------------|--------------------------------------------------------------|
| id               | BIGINT       | PRIMARY KEY, AUTO_INCREMENT                                  |
| patient_id       | BIGINT       | NOT NULL, FOREIGN KEY REFERENCES patients(id) ON DELETE CASCADE |
| doctor_id        | BIGINT       | NOT NULL, FOREIGN KEY REFERENCES doctors(id) ON DELETE CASCADE  |
| appointment_date | DATE         | NOT NULL                                                     |
| appointment_time | TIME         | NOT NULL                                                     |
| status           | ENUM         | NOT NULL, DEFAULT 'PENDING' — values: PENDING, CONFIRMED, COMPLETED, CANCELLED |
| reason           | VARCHAR(255) |                                                              |
| created_at       | TIMESTAMP    | DEFAULT CURRENT_TIMESTAMP                                    |

---

## MongoDB Collection Design

### Collection: `prescriptions`

Prescriptions are document-based because their structure varies significantly between appointments — medications, dosage instructions, and follow-up notes are nested and variable in length, making a rigid relational schema impractical.

Each document is linked to a MySQL `appointments` record via `appointmentId`.

**Example Document:**

```json
{
  "_id": { "$oid": "664f2a3b1c9d4e0012a3b456" },
  "appointmentId": 42,
  "patientId": 7,
  "doctorId": 3,
  "issuedAt": "2026-05-07T10:30:00Z",
  "diagnosis": "Acute sinusitis with mild fever",
  "medications": [
    {
      "name": "Amoxicillin",
      "dosage": "500mg",
      "frequency": "3 times daily",
      "durationDays": 7,
      "instructions": "Take after meals"
    },
    {
      "name": "Paracetamol",
      "dosage": "650mg",
      "frequency": "As needed (max 4 times daily)",
      "durationDays": 5,
      "instructions": "Take only when fever exceeds 38°C"
    }
  ],
  "followUpRequired": true,
  "followUpDate": "2026-05-14",
  "notes": "Patient advised to rest and increase fluid intake. Review if symptoms persist beyond 5 days."
}
```

**Design decisions:**
- `medications` is an array of embedded objects — avoids a separate `prescription_items` join table and keeps the full prescription readable as one document.
- `appointmentId` is a reference back to MySQL so the REST layer can join prescription data with appointment context when needed.
- `followUpRequired` and `followUpDate` are kept here rather than in MySQL because they are clinically part of the prescription, not the scheduling system.

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
