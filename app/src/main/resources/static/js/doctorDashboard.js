// doctorDashboard.js

import { getAllAppointments } from './services/appointmentServices.js';
import { createPatientRow } from './patientRow.js';

// Get table body where patient rows will be added
const patientTableBody = document.getElementById("patientTableBody");

// Initialize selectedDate to today in YYYY-MM-DD format
let selectedDate = new Date().toISOString().split("T")[0];

// Get auth token from localStorage
const token = localStorage.getItem("token");

// Initialize patientName filter
let patientName = null;


// Search bar - filter by patient name
document.getElementById("searchBar").addEventListener("input", (e) => {
  const value = e.target.value.trim();

  if (value) {
    patientName = value;
  } else {
    patientName = "null";
  }

  loadAppointments();
});


// Today button - reset to today's date
document.getElementById("todayButton").addEventListener("click", () => {
  selectedDate = new Date().toISOString().split("T")[0];

  const datePicker = document.getElementById("datePicker");
  if (datePicker) datePicker.value = selectedDate;

  loadAppointments();
});


// Date picker - update selected date
document.getElementById("datePicker").addEventListener("change", (e) => {
  selectedDate = e.target.value;
  loadAppointments();
});


// Load and display appointments
async function loadAppointments() {
  try {
    // Step 1: Fetch appointments
    const appointments = await getAllAppointments(selectedDate, patientName, token);

    // Step 2: Clear existing rows
    patientTableBody.innerHTML = "";

    // Step 3: No appointments found
    if (!appointments || appointments.length === 0) {
      patientTableBody.innerHTML = `
        <tr>
          <td colspan="5" class="noPatientRecord">No Appointments found for today.</td>
        </tr>`;
      return;
    }

    // Step 4: Render each appointment as a row
    appointments.forEach(appointment => {
      const patient = {
        id:    appointment.patientId,
        name:  appointment.patientName,
        phone: appointment.patientPhone,
        email: appointment.patientEmail
      };

      const row = createPatientRow(patient, appointment);
      patientTableBody.appendChild(row);
    });

  } catch (error) {
    // Step 5: Handle errors
    console.error("Error loading appointments:", error);
    patientTableBody.innerHTML = `
      <tr>
        <td colspan="5" class="noPatientRecord">Error loading appointments. Try again later.</td>
      </tr>`;
  }
}


// Initialise on page load
document.addEventListener("DOMContentLoaded", () => {
  renderContent();
  loadAppointments();
});