// adminDashboard.js

import { openModal, closeModal } from './components/modals.js';
import { getDoctors, filterDoctors, saveDoctor } from './services/doctorServices.js';
import { createDoctorCard } from './components/doctorCard.js';

// Expose openModal globally so header.js onclick="openModal('addDoctor')" works
window.openModal = openModal;


// When DOM is fully loaded, fetch and display all doctors
document.addEventListener("DOMContentLoaded", () => {
  loadDoctorCards();
});


// Load all doctor cards
async function loadDoctorCards() {
  try {
    const doctors = await getDoctors();
    const content = document.getElementById("content");
    content.innerHTML = "";

    doctors.forEach(doctor => {
      const card = createDoctorCard(doctor);
      content.appendChild(card);
    });

  } catch (error) {
    console.error("Error loading doctor cards:", error);
  }
}


// Attach event listeners to search bar and filter dropdowns
document.getElementById("searchBar").addEventListener("input", filterDoctorsOnChange);
document.getElementById("timeFilter").addEventListener("change", filterDoctorsOnChange);
document.getElementById("specialtyFilter").addEventListener("change", filterDoctorsOnChange);


// Filter doctors based on name, time, and specialty
async function filterDoctorsOnChange() {
  try {
    const name      = document.getElementById("searchBar").value.trim() || null;
    const time      = document.getElementById("timeFilter").value || null;
    const specialty = document.getElementById("specialtyFilter").value || null;

    const doctors = await filterDoctors(name, time, specialty);
    const content = document.getElementById("content");

    if (doctors && doctors.length > 0) {
      renderDoctorCards(doctors);
    } else {
      content.innerHTML = "<p class='noPatientRecord'>No doctors found with the given filters.</p>";
    }

  } catch (error) {
    console.error("Error filtering doctors:", error);
    alert("An error occurred while filtering doctors.");
  }
}


// Helper: render a list of doctor cards
function renderDoctorCards(doctors) {
  const content = document.getElementById("content");
  content.innerHTML = "";

  doctors.forEach(doctor => {
    const card = createDoctorCard(doctor);
    content.appendChild(card);
  });
}


// Add a new doctor from modal form data
window.adminAddDoctor = async function () {

  // Collect form values — IDs match what modals.js renders
  const name      = document.getElementById("doctorName").value.trim();
  const email     = document.getElementById("doctorEmail").value.trim();
  const phone     = document.getElementById("doctorPhone").value.trim();
  const password  = document.getElementById("doctorPassword").value.trim();
  const specialty = document.getElementById("specialization").value.trim();
  const checkedBoxes   = document.querySelectorAll('input[name="availability"]:checked');
  const availableTimes = Array.from(checkedBoxes).map(cb => cb.value);

  // Get auth token
  const token = localStorage.getItem("token");
  if (!token) {
    alert("Unauthorised. Please log in as admin.");
    return;
  }

  // Build doctor object
  const doctor = { name, email, phone, password, specialty, availableTimes };

  // Save doctor via service
  const result = await saveDoctor(doctor, token);

  if (result.success) {
    alert("Doctor added successfully.");
    closeModal();
    location.reload();
  } else {
    alert("Failed to add doctor: " + result.message);
  }
};
