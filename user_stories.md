# Patient Appointment Portal - User Stories

## Admin User Stories

### Story 1: Manage User Accounts
**As an** Admin  
**I want to** create, view, update, and delete user accounts for doctors and patients  
**So that** I can maintain an organized system and control who has access to the portal

**Acceptance Criteria:**
- Admin can create new doctor and patient accounts with email and basic information
- Admin can view a list of all registered users with their roles and status
- Admin can update user details (name, contact information, role)
- Admin can deactivate or delete user accounts
- System confirms successful account actions with notifications

---

### Story 2: Manage System Settings
**As an** Admin  
**I want to** configure system settings such as appointment duration, working hours, and holidays  
**So that** the system operates according to organizational policies

**Acceptance Criteria:**
- Admin can set default appointment duration (e.g., 15, 30, 60 minutes)
- Admin can define clinic operating hours (opening and closing times)
- Admin can mark holidays when no appointments are available
- Admin can enable/disable features (e.g., patient self-booking)
- Changes take effect immediately across the system

---

### Story 3: View System Analytics
**As an** Admin  
**I want to** view reports and analytics on appointment usage, no-shows, and system performance  
**So that** I can monitor the health of the portal and identify areas for improvement

**Acceptance Criteria:**
- Admin can view appointment statistics (total booked, completed, cancelled, no-show)
- Admin can filter reports by date range, doctor, or specialty
- Admin can see no-show rates and patterns
- Admin can export reports in PDF or CSV format
- Dashboard displays key metrics at a glance

---

### Story 4: Manage Doctor Specialties
**As an** Admin  
**I want to** create and assign specialties to doctors (e.g., Cardiology, Pediatrics)  
**So that** patients can easily find doctors matching their needs

**Acceptance Criteria:**
- Admin can create new specialty categories
- Admin can assign multiple specialties to each doctor
- Specialties are searchable and filterable by patients
- Admin can edit or remove specialties from doctors
- Specialty changes are reflected in real-time

---

## Patient User Stories

### Story 1: Search and View Available Appointments
**As a** Patient  
**I want to** search for available appointments by doctor name, specialty, or date  
**So that** I can easily find a doctor and time slot that suits my needs

**Acceptance Criteria:**
- Patient can search by doctor name or medical specialty
- Patient can filter results by date range and time of day
- System displays available time slots in a calendar view
- Each appointment shows doctor name, specialty, and duration
- Patient can see doctor ratings and patient reviews

---

### Story 2: Book an Appointment
**As a** Patient  
**I want to** book an appointment with a doctor by selecting a time slot and confirming the booking  
**So that** I can secure a spot for my medical consultation

**Acceptance Criteria:**
- Patient can select an available time slot
- System shows appointment details before confirmation (date, time, doctor, location)
- Patient can add notes or reason for visit
- Booking is confirmed with a confirmation number
- Patient receives email/SMS confirmation with appointment details
- Appointment appears on patient dashboard

---

### Story 3: View and Manage My Appointments
**As a** Patient  
**I want to** view all my upcoming and past appointments in one place  
**So that** I can easily track my medical visits and prepare accordingly

**Acceptance Criteria:**
- Patient dashboard displays upcoming appointments in chronological order
- Each appointment shows date, time, doctor name, specialty, and location
- Patient can view past appointment history
- Patient can see appointment status (confirmed, completed, cancelled)
- System displays clear reminders for upcoming appointments

---

### Story 4: Cancel or Reschedule Appointment
**As a** Patient  
**I want to** cancel or reschedule my appointment with a specific notice period  
**So that** I can adjust my schedule if my plans change

**Acceptance Criteria:**
- Patient can cancel appointments with at least 24 hours notice
- Patient can select a new available time slot to reschedule
- System displays any cancellation policies or fees if applicable
- Cancellation/reschedule confirmation is sent via email
- Doctor calendar is updated immediately
- Patient can see cancellation reason options

---

### Story 5: Receive Appointment Reminders
**As a** Patient  
**I want to** receive reminders before my appointment  
**So that** I don't forget my scheduled visit

**Acceptance Criteria:**
- Patient receives reminder notifications 24 hours before appointment
- Patient receives reminder notification 1 hour before appointment
- Reminders can be delivered via email, SMS, or in-app notification
- Patient can customize reminder preferences
- Patient can opt out of specific reminder types

---

## Doctor User Stories

### Story 1: View My Schedule
**As a** Doctor  
**I want to** view my complete appointment schedule for the day, week, or month  
**So that** I can plan my time and prepare for patient visits

**Acceptance Criteria:**
- Doctor dashboard displays appointments in calendar view
- Doctor can switch between day, week, and month views
- Each appointment shows patient name, appointment time, and reason for visit
- System highlights back-to-back appointments
- Doctor can see patient notes and medical history for upcoming appointments
- Schedule is color-coded by appointment status (confirmed, completed, cancelled)

---

### Story 2: Manage My Availability
**As a** Doctor  
**I want to** set and update my availability for appointments (working hours, days off, blocked time)  
**So that** patients can only book appointments when I'm available

**Acceptance Criteria:**
- Doctor can set weekly recurring availability (e.g., Monday-Friday 9am-5pm)
- Doctor can mark specific dates/times as unavailable
- Doctor can block time for lunch, meetings, or other activities
- Doctor can set vacation or leave periods
- Changes to availability take effect immediately
- System prevents patient bookings outside available times

---

### Story 3: Accept or Decline Pending Appointments
**As a** Doctor  
**I want to** review and accept or decline appointment requests from patients  
**So that** I can control which appointments I fulfill

**Acceptance Criteria:**
- Doctor receives notifications for new appointment requests
- Doctor can view pending appointments with patient information
- Doctor can accept appointment with one click
- Doctor can decline appointment and provide a reason (optional)
- Patient is notified of acceptance or decline via email
- Declined appointments return to available slots for other doctors

---

### Story 4: Add Notes to Patient Appointments
**As a** Doctor  
**I want to** add and update notes during or after an appointment  
**So that** I can document patient interactions and treatment plans

**Acceptance Criteria:**
- Doctor can add appointment notes before, during, or after the visit
- Notes are saved to the patient's appointment record
- Doctor can edit notes until the appointment is marked complete
- Notes are private and only visible to medical staff
- System timestamps all note entries
- Doctor can attach files or prescriptions to appointments

---

### Story 5: View Patient History
**As a** Doctor  
**I want to** view a patient's appointment history and previous notes  
**So that** I can understand their medical background and provide continuity of care

**Acceptance Criteria:**
- Doctor can access patient profile from appointment view
- Patient profile displays all past appointments and dates
- Doctor can view notes from previous appointments with the same patient
- Patient profile shows any recurring conditions or notes from other doctors
- Information is organized chronologically
- System protects patient privacy with appropriate access controls

---

### Story 6: Generate Appointment Reports
**As a** Doctor  
**I want to** view reports on my appointment activity (booked, completed, cancelled, no-shows)  
**So that** I can track my performance and identify patterns

**Acceptance Criteria:**
- Doctor can view monthly/quarterly appointment statistics
- Reports show appointment count by status (completed, no-show, cancelled)
- Doctor can filter reports by date range
- System shows no-show rate and trends
- Doctor can export reports for personal records
- Comparison with previous periods is available
