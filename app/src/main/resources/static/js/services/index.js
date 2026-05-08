// index.js

// Imports
import { openModal } from './render.js';
import { BASE_URL } from './config.js';

// Define API endpoints
const ADMIN_API = `${BASE_URL}/admin/login`;
const DOCTOR_API = `${BASE_URL}/doctor/login`;

// Attach button listeners after DOM is ready
window.onload = function () {
  const adminLoginBtn = document.getElementById("adminLogin");
  const doctorLoginBtn = document.getElementById("doctorLogin");

  if (adminLoginBtn) {
    adminLoginBtn.addEventListener("click", () => openModal('adminLogin'));
  }

  if (doctorLoginBtn) {
    doctorLoginBtn.addEventListener("click", () => openModal('doctorLogin'));
  }
};

// Admin Login Handler
window.adminLoginHandler = async function () {

  // Step 1: Get credentials from input fields
  const username = document.getElementById("adminUsername").value;
  const password = document.getElementById("adminPassword").value;

  // Step 2: Create admin object
  const admin = { username, password };

  try {
    // Step 3: POST request to admin login endpoint
    const response = await fetch(ADMIN_API, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(admin)
    });

    // Step 4: Handle successful response
    if (response.ok) {
      const data = await response.json();
      localStorage.setItem("token", data.token);
      selectRole('admin');
    } else {
      // Step 5: Handle failed login
      alert("Invalid admin credentials. Please try again.");
    }

  } catch (error) {
    // Step 6: Handle network/server errors
    console.error("Admin login error:", error);
    alert("An error occurred during admin login. Please try again.");
  }
};

// Doctor Login Handler
window.doctorLoginHandler = async function () {

  // Step 1: Get credentials from input fields
  const email = document.getElementById("doctorEmail").value;
  const password = document.getElementById("doctorPassword").value;

  // Step 2: Create doctor object
  const doctor = { email, password };

  try {
    // Step 3: POST request to doctor login endpoint
    const response = await fetch(DOCTOR_API, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(doctor)
    });

    // Step 4: Handle successful response
    if (response.ok) {
      const data = await response.json();
      localStorage.setItem("token", data.token);
      selectRole('doctor');
    } else {
      // Step 5: Handle failed login
      alert("Invalid doctor credentials. Please try again.");
    }

  } catch (error) {
    // Step 6: Handle network/server errors
    console.error("Doctor login error:", error);
    alert("An error occurred during doctor login. Please try again.");
  }
};