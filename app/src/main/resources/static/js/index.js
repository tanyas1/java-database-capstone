 const BASE_URL = window.location.origin;                                                                                                                                                                                               
   
  function openLoginModal(role) {                                                                                                                                                                                                        
    const content = document.getElementById('modal-content');                                                                                                                                                                          
    if (role === 'admin') {                                                                                                                                                                                                              
      content.innerHTML = `                                                                                                                                                                                                            
        <h2>Admin Login</h2>
        <input type="text" id="adminUsername" placeholder="Username" class="input-field">
        <input type="password" id="adminPassword" placeholder="Password" class="input-field">                                                                                                                                            
        <button class="dashboard-btn" onclick="handleAdminLogin()">Login</button>`;                                                                                                                                                      
    } else if (role === 'doctor') {                                                                                                                                                                                                      
      content.innerHTML = `                                                                                                                                                                                                              
        <h2>Doctor Login</h2>                                                                                                                                                                                                          
        <input type="email" id="doctorEmail" placeholder="Email" class="input-field">                                                                                                                                                    
        <input type="password" id="doctorPassword" placeholder="Password" class="input-field">                                                                                                                                         
        <button class="dashboard-btn" onclick="handleDoctorLogin()">Login</button>`;                                                                                                                                                     
    } else if (role === 'patient') {
      content.innerHTML = `                                                                                                                                                                                                              
        <h2>Patient</h2>                                                                                                                                                                                                                 
        <button class="dashboard-btn" onclick="openPatientLogin()">Login</button>
        <button class="dashboard-btn" onclick="openPatientSignup()">Sign Up</button>`;                                                                                                                                                   
    }                                                                                                                                                                                                                                    
    document.getElementById('modal-overlay').style.display = 'flex';
  }                                                                                                                                                                                                                                      
                                                                                                                                                                                                                                       
  function closeModal() {
    document.getElementById('modal-overlay').style.display = 'none';
  }                                                                                                                                                                                                                                      
   
  function openPatientLogin() {                                                                                                                                                                                                          
    document.getElementById('modal-content').innerHTML = `                                                                                                                                                                             
      <h2>Patient Login</h2>
      <input type="email" id="patientEmail" placeholder="Email" class="input-field">
      <input type="password" id="patientPassword" placeholder="Password" class="input-field">                                                                                                                                            
      <button class="dashboard-btn" onclick="handlePatientLogin()">Login</button>`;
  }                                                                                                                                                                                                                                      
                                                                                                                                                                                                                                       
  function openPatientSignup() {                                                                                                                                                                                                         
    document.getElementById('modal-content').innerHTML = `                                                                                                                                                                             
      <h2>Patient Sign Up</h2>
      <input type="text" id="patientName" placeholder="Name" class="input-field">
      <input type="email" id="patientEmail" placeholder="Email" class="input-field">                                                                                                                                                     
      <input type="password" id="patientPassword" placeholder="Password" class="input-field">
      <input type="text" id="patientPhone" placeholder="Phone" class="input-field">                                                                                                                                                      
      <input type="text" id="patientAddress" placeholder="Address" class="input-field">                                                                                                                                                  
      <button class="dashboard-btn" onclick="handlePatientSignup()">Sign Up</button>`;
  }                                                                                                                                                                                                                                      
                                                                                                                                                                                                                                       
  async function handleAdminLogin() {                                                                                                                                                                                                    
    const username = document.getElementById('adminUsername').value;                                                                                                                                                                   
    const password = document.getElementById('adminPassword').value;
    const response = await fetch(`${BASE_URL}/admin/login`, {
      method: 'POST', headers: { 'Content-Type': 'application/json' },                                                                                                                                                                   
      body: JSON.stringify({ username, password })
    });                                                                                                                                                                                                                                  
    if (response.ok) {                                                                                                                                                                                                                 
      const data = await response.json();                                                                                                                                                                                                
      localStorage.setItem('token', data.token);
      localStorage.setItem('userRole', 'admin');                                                                                                                                                                                         
      window.location.href = `/adminDashboard/${data.token}`;                                                                                                                                                                          
    } else { alert('Invalid admin credentials.'); }                                                                                                                                                                                      
  }
                                                                                                                                                                                                                                         
  async function handleDoctorLogin() {                                                                                                                                                                                                   
    const email = document.getElementById('doctorEmail').value;
    const password = document.getElementById('doctorPassword').value;                                                                                                                                                                    
    const response = await fetch(`${BASE_URL}/doctor/login`, {                                                                                                                                                                         
      method: 'POST', headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ email, password })                                                                                                                                                                                          
    });
    if (response.ok) {                                                                                                                                                                                                                   
      const data = await response.json();                                                                                                                                                                                              
      localStorage.setItem('token', data.token);
      localStorage.setItem('userRole', 'doctor');
      window.location.href = `/doctorDashboard/${data.token}`;                                                                                                                                                                           
    } else { alert('Invalid doctor credentials.'); }
  }                                                                                                                                                                                                                                      
                                                                                                                                                                                                                                       
  async function handlePatientLogin() {
    const email = document.getElementById('patientEmail').value;
    const password = document.getElementById('patientPassword').value;                                                                                                                                                                   
    const response = await fetch(`${BASE_URL}/patient/login`, {
      method: 'POST', headers: { 'Content-Type': 'application/json' },                                                                                                                                                                   
      body: JSON.stringify({ email, password })                                                                                                                                                                                          
    });
    if (response.ok) {                                                                                                                                                                                                                   
      const data = await response.json();                                                                                                                                                                                              
      localStorage.setItem('token', data.token);
      localStorage.setItem('userRole', 'patient');
      window.location.href = '/pages/loggedPatientDashboard.html';
    } else { alert('Invalid patient credentials.'); }                                                                                                                                                                                    
  }                                                                                                                                                                                                                                      
                                                                                                                                                                                                                                         
  async function handlePatientSignup() {                                                                                                                                                                                                 
    const name = document.getElementById('patientName').value;                                                                                                                                                                         
    const password = document.getElementById('patientPassword').value;
    const phone = document.getElementById('patientPhone').value;      
    const address = document.getElementById('patientAddress').value;
    const response = await fetch(`${BASE_URL}/patient`, {                                                                                                                                                                                
      method: 'POST', headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ name, email, password, phone, address })                                                                                                                                                                    
    });                                                                                                                                                                                                                                  
    if (response.ok) {
      alert('Registration successful! Please log in.');                                                                                                                                                                                  
      openLoginModal('patient');                                                                                                                                                                                                       
      openPatientLogin();       
    } else { alert('Registration failed. Email or phone may already be in use.'); }                                                                                                                                                      
  }
