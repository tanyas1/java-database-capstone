// doctorCard.js

// Imports
import { showBookingOverlay } from '../loggedPatient.js';
import { deleteDoctor } from '../services/doctorServices.js';
import { getPatientData } from '../services/patientServices.js';

// Function to create and return a DOM element for a single doctor card
function createDoctorCard(doctor) {

  // Create the main container for the doctor card
  const card = document.createElement("div");
  card.classList.add("doctor-card");

  // Retrieve the current user role from localStorage
  const role = localStorage.getItem("userRole");

  // Create a div to hold doctor information
  const infoDiv = document.createElement("div");
  infoDiv.classList.add("doctor-info");

  // Create and set the doctor's name
  const name = document.createElement("h3");
  name.textContent = doctor.name;

  // Create and set the doctor's specialization
  const specialization = document.createElement("p");
  specialization.textContent = `Specialization: ${doctor.specialization}`;

  // Create and set the doctor's email
  const email = document.createElement("p");
  email.textContent = `Email: ${doctor.email}`;

  // Create and list available appointment times
  const times = document.createElement("ul");
  times.classList.add("available-times");
  if (doctor.availableTimes && doctor.availableTimes.length > 0) {
    doctor.availableTimes.forEach(time => {
      const timeItem = document.createElement("li");
      timeItem.textContent = time;
      times.appendChild(timeItem);
    });
  } else {
    const noTimes = document.createElement("li");
    noTimes.textContent = "No available times";
    times.appendChild(noTimes);
  }

  // Append all info elements to the doctor info container
  infoDiv.appendChild(name);
  infoDiv.appendChild(specialization);
  infoDiv.appendChild(email);
  infoDiv.appendChild(times);

  // Create a container for card action buttons
  const actionsDiv = document.createElement("div");
  actionsDiv.classList.add("card-actions");

  // === ADMIN ROLE ACTIONS ===
  if (role === "admin") {

    // Create a delete button
    const deleteBtn = document.createElement("button");
    deleteBtn.textContent = "Delete";
    deleteBtn.classList.add("delete-btn");

    // Add click handler for delete button
    deleteBtn.addEventListener("click", async () => {

      // Get the admin token from localStorage
      const token = localStorage.getItem("token");

      // Call API to delete the doctor
      const result = await deleteDoctor(doctor.id, token);

      // Show result and remove card if successful
      if (result.success) {
        alert("Doctor deleted successfully.");
        card.remove();
      } else {
        alert("Failed to delete doctor: " + result.message);
      }
    });

    // Add delete button to actions container
    actionsDiv.appendChild(deleteBtn);

  // === PATIENT (NOT LOGGED-IN) ROLE ACTIONS ===
  } else if (role === "patient") {

    // Create a book now button
    const bookBtn = document.createElement("button");
    bookBtn.textContent = "Book Now";
    bookBtn.classList.add("book-btn");

    // Alert patient to log in before booking
    bookBtn.addEventListener("click", () => {
      alert("Please log in to book an appointment.");
    });

    // Add button to actions container
    actionsDiv.appendChild(bookBtn);

  // === LOGGED-IN PATIENT ROLE ACTIONS ===
  } else if (role === "loggedPatient") {

    // Create a book now button
    const bookBtn = document.createElement("button");
    bookBtn.textContent = "Book Now";
    bookBtn.classList.add("book-btn");

    // Handle booking logic for logged-in patient
    bookBtn.addEventListener("click", async () => {

      // Redirect if token not available
      const token = localStorage.getItem("token");
      if (!token) {
        alert("Session expired. Please log in again.");
        window.location.href = "/";
        return;
      }

      // Fetch patient data with token
      const patient = await getPatientData(token);

      // Show booking overlay UI with doctor and patient info
      showBookingOverlay(doctor, patient);
    });

    // Add button to actions container
    actionsDiv.appendChild(bookBtn);
  }

  // Append doctor info and action buttons to the card
  card.appendChild(infoDiv);
  card.appendChild(actionsDiv);

  // Return the complete doctor card element
  return card;
}

export { createDoctorCard };