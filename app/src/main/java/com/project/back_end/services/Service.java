package com.project.back_end.services;

import com.project.back_end.models.Appointment;
import com.project.back_end.models.Doctor;
import com.project.back_end.models.Patient;
import com.project.back_end.models.Admin;
import com.project.back_end.repo.AdminRepository;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.PatientRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@org.springframework.stereotype.Service
public class Service {

    private final TokenService tokenService;
    private final AdminRepository adminRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;
    private final DoctorService doctorService;
    private final PatientService patientService;

    // 2. Constructor Injection
    public Service(TokenService tokenService,
                   AdminRepository adminRepository,
                   DoctorRepository doctorRepository,
                   PatientRepository patientRepository,
                   AppointmentRepository appointmentRepository,
                   DoctorService doctorService,
                   PatientService patientService) {
        this.tokenService          = tokenService;
        this.adminRepository       = adminRepository;
        this.doctorRepository      = doctorRepository;
        this.patientRepository     = patientRepository;
        this.appointmentRepository = appointmentRepository;
        this.doctorService         = doctorService;
        this.patientService        = patientService;
    }


    // 3. Validate Token
    public int validateToken(String token, String role) {
        try {
            boolean valid = tokenService.validateToken(token, role);
            return valid ? 1 : 0;
        } catch (Exception e) {
            return 0;
        }
    }


    // 4. Validate Admin Login
    public ResponseEntity<Map<String, Object>> validateAdmin(String username, String password) {
        Map<String, Object> response = new HashMap<>();
        try {
            Admin admin = adminRepository.findByUsername(username);

            if (admin == null) {
                response.put("message", "Admin not found.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            if (!admin.getPassword().equals(password)) {
                response.put("message", "Invalid password.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            String token = tokenService.generateToken(username);
            response.put("token", token);
            response.put("message", "Login successful.");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.err.println("Error validating admin: " + e.getMessage());
            response.put("message", "Internal server error.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    // 5. Filter Doctors
    public ResponseEntity<Map<String, Object>> filterDoctor(String name, String specialty, String time) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Doctor> doctors;

            boolean hasName      = name != null && !name.isEmpty();
            boolean hasSpecialty = specialty != null && !specialty.isEmpty();
            boolean hasTime      = time != null && !time.isEmpty();

            if (hasName && hasSpecialty && hasTime) {
                doctors = doctorService.filterDoctorsByNameSpecilityandTime(name, specialty, time);
            } else if (hasName && hasSpecialty) {
                doctors = doctorService.filterDoctorByNameAndSpecility(name, specialty);
            } else if (hasName && hasTime) {
                doctors = doctorService.filterDoctorByNameAndTime(name, time);
            } else if (hasSpecialty && hasTime) {
                doctors = doctorService.filterDoctorByTimeAndSpecility(specialty, time);
            } else if (hasName) {
                doctors = doctorService.findDoctorByName(name);
            } else if (hasSpecialty) {
                doctors = doctorService.filterDoctorBySpecility(specialty);
            } else if (hasTime) {
                doctors = doctorService.filterDoctorsByTime(time);
            } else {
                doctors = doctorService.getDoctors();
            }

            response.put("doctors", doctors);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.err.println("Error filtering doctors: " + e.getMessage());
            response.put("message", "Error filtering doctors.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    // 6. Validate Appointment Time
    public int validateAppointment(Long doctorId, LocalDateTime appointmentTime) {
        try {
            Doctor doctor = doctorRepository.findById(doctorId).orElse(null);
            if (doctor == null) return -1;

            LocalDate date = appointmentTime.toLocalDate();
            List<String> available = doctorService.getDoctorAvailability(doctorId, date);

            String requestedTime = String.format("%02d:%02d",
                    appointmentTime.getHour(), appointmentTime.getMinute());

            boolean match = available.stream()
                    .anyMatch(slot -> slot.startsWith(requestedTime));

            return match ? 1 : 0;

        } catch (Exception e) {
            System.err.println("Error validating appointment: " + e.getMessage());
            return 0;
        }
    }


    // 7. Validate Patient (check for duplicates)
    public boolean validatePatient(String email, String phone) {
        try {
            Patient existing = patientRepository.findByEmailOrPhone(email, phone);
            return existing == null;
        } catch (Exception e) {
            System.err.println("Error validating patient: " + e.getMessage());
            return false;
        }
    }


    // 8. Validate Patient Login
    public ResponseEntity<Map<String, Object>> validatePatientLogin(String email, String password) {
        Map<String, Object> response = new HashMap<>();
        try {
            Patient patient = patientRepository.findByEmail(email);

            if (patient == null) {
                response.put("message", "Patient not found.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            if (!patient.getPassword().equals(password)) {
                response.put("message", "Invalid password.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            String token = tokenService.generateToken(email);
            response.put("token", token);
            response.put("message", "Login successful.");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.err.println("Error validating patient login: " + e.getMessage());
            response.put("message", "Internal server error.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    // 9. Filter Patient Appointments
    public ResponseEntity<Map<String, Object>> filterPatient(String token,
                                                              String condition,
                                                              String doctorName) {
        try {
            String email = tokenService.extractEmail(token);
            Patient patient = patientRepository.findByEmail(email);

            if (patient == null) {
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Patient not found.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            Long patientId = patient.getId();

            boolean hasCondition  = condition != null && !condition.isEmpty();
            boolean hasDoctorName = doctorName != null && !doctorName.isEmpty();

            if (hasCondition && hasDoctorName) {
                return patientService.filterByDoctorAndCondition(patientId, doctorName, condition);
            } else if (hasCondition) {
                return patientService.filterByCondition(patientId, condition);
            } else if (hasDoctorName) {
                return patientService.filterByDoctor(patientId, doctorName);
            } else {
                return patientService.getPatientAppointment(patientId);
            }

        } catch (Exception e) {
            System.err.println("Error filtering patient appointments: " + e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Internal server error.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}