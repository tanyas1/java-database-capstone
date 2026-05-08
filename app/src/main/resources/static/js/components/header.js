// header.js

// 1. Define the renderHeader Function
function renderHeader() {

  // 2. Select the Header Div
  const headerDiv = document.getElementById("header");

  // 3. Check if Current Page is Root
  if (window.location.pathname.endsWith("/")) {
    localStorage.removeItem("userRole");
    headerDiv.innerHTML = `
      <header class="header">
        <div class="logo-section">
          <img src="../assets/images/logo/logo.png" alt="Hospital CRM Logo" class="logo-img">
          <span class="logo-title">Hospital CMS</span>
        </div>
      </header>`;
    return;
  }

  // 4. Retrieve Role and Token from LocalStorage
  const role = localStorage.getItem("userRole");
  const token = localStorage.getItem("token");

  // 5. Initialize Header Content
  let headerContent = `
    <header class="header">
      <div class="logo-section">
        <img src="../assets/images/logo/logo.png" alt="Hospital CRM Logo" class="logo-img">
        <span class="logo-title">Hospital CMS</span>
      </div>
      <nav>`;

  // 6. Handle Session Expiry or Invalid Login
  if ((role === "loggedPatient" || role === "admin" || role === "doctor") && !token) {
    localStorage.removeItem("userRole");
    alert("Session expired or invalid login. Please log in again.");
    window.location.href = "/";
    return;
  }

  // 7. Add Role-Specific Header Content
  if (role === "admin") {
    headerContent += `
      <button id="addDocBtn" class="adminBtn" onclick="openModal('addDoctor')">Add Doctor</button>
      <a href="#" onclick="logout()">Logout</a>`;
  } else if (role === "doctor") {
    headerContent += `
      <button class="adminBtn" onclick="selectRole('doctor')">Home</button>
      <a href="#" onclick="logout()">Logout</a>`;
  } else if (role === "patient") {
    headerContent += `
      <button id="patientLogin" class="adminBtn">Login</button>
      <button id="patientSignup" class="adminBtn">Sign Up</button>`;
  } else if (role === "loggedPatient") {
    headerContent += `
      <button id="home" class="adminBtn" onclick="window.location.href='/pages/loggedPatientDashboard.html'">Home</button>
      <button id="patientAppointments" class="adminBtn" onclick="window.location.href='/pages/patientAppointments.html'">Appointments</button>
      <a href="#" onclick="logoutPatient()">Logout</a>`;
  }

  // 9. Close the Header Section
  headerContent += `
      </nav>
    </header>`;

  // 10. Render the Header Content
  headerDiv.innerHTML = headerContent;

  // 11. Attach Event Listeners
  attachHeaderButtonListeners();
}

// 13. attachHeaderButtonListeners
function attachHeaderButtonListeners() {
  const patientLogin = document.getElementById("patientLogin");
  const patientSignup = document.getElementById("patientSignup");

  if (patientLogin) {
    patientLogin.addEventListener("click", () => openModal("patientLogin"));
  }
  if (patientSignup) {
    patientSignup.addEventListener("click", () => openModal("patientSignup"));
  }
}

// 14. logout
function logout() {
  localStorage.removeItem("userRole");
  localStorage.removeItem("token");
  window.location.href = "/";
}

// 15. logoutPatient
function logoutPatient() {
  localStorage.removeItem("token");
  window.location.href = "/pages/patientDashboard.html";
}

// 16. Initialise
renderHeader();