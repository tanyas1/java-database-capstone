// doctorServices.js

import { API_BASE_URL } from "../config/config.js";

const DOCTOR_API = API_BASE_URL + '/doctor';


// 1. Get All Doctors
async function getDoctors() {
  try {
    const response = await fetch(DOCTOR_API);
    return await response.json();
  } catch (error) {
    console.error("Error fetching doctors:", error);
    return [];
  }
}


// 2. Delete a Doctor
async function deleteDoctor(id, token) {
  try {
    const response = await fetch(`${DOCTOR_API}/${id}`, {
      method: "DELETE",
      headers: {
        "Authorization": `Bearer ${token}`,
        "Content-Type": "application/json"
      }
    });

    const data = await response.json();
    return {
      success: response.ok,
      message: data.message || "Doctor deleted successfully."
    };

  } catch (error) {
    console.error("Error deleting doctor:", error);
    return { success: false, message: "An error occurred while deleting the doctor." };
  }
}


// 3. Add a New Doctor
async function saveDoctor(doctor, token) {
  try {
    const response = await fetch(DOCTOR_API, {
      method: "POST",
      headers: {
        "Authorization": `Bearer ${token}`,
        "Content-Type": "application/json"
      },
      body: JSON.stringify(doctor)
    });

    const data = await response.json();
    return {
      success: response.ok,
      message: data.message || "Doctor saved successfully."
    };

  } catch (error) {
    console.error("Error saving doctor:", error);
    return { success: false, message: "An error occurred while saving the doctor." };
  }
}


// 4. Filter Doctors
async function filterDoctors(name, time, specialty) {
  try {
    const params = new URLSearchParams();
    if (name)      params.append("name", name);
    if (time)      params.append("time", time);
    if (specialty) params.append("specialty", specialty);

    const response = await fetch(`${DOCTOR_API}/filter?${params.toString()}`);
    return await response.json();

  } catch (error) {
    console.error("Error filtering doctors:", error);
    return [];
  }
}


export { getDoctors, deleteDoctor, saveDoctor, filterDoctors };