package com.project.back_end.controllers;

import com.project.back_end.DTO.Login;
import com.project.back_end.models.Patient;
import com.project.back_end.services.PatientService;
import com.project.back_end.services.Service;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("${api.path}patient")
public class PatientController {

    private final PatientService patientService;
    private final Service service;

    // 2. Constructor Injection
    public PatientController(PatientService patientService, Service service) {
        this.patientService = patientService;
        this.service        = service;
    }


    // 3. Get Patient Details
    @GetMapping("/{token}")
    public ResponseEntity<Map<String, Object>> getPatient(@PathVariable String token) {
        Map<String, Object> response = new HashMap<>();

        int tokenValid = service.validateToken(token, "patient");
        if (tokenValid != 1) {
            response.put("message", "Unauthorized: Invalid or expired token.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        return patientService.getPatientDetails(token);
    }


    // 4. Create Patient
    @PostMapping
    public ResponseEntity<Map<String, Object>> createPatient(@Valid @RequestBody Patient patient) {
        Map<String, Object> response = new HashMap<>();

        // Check if patient already exists
        boolean isValid = service.validatePatient(patient.getEmail(), patient.getPhone());
        if (!isValid) {
            response.put("message", "Patient with this email or phone already exists.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        int result = patientService.createPatient(patient);
        if (result == 1) {
            response.put("message", "Patient registered successfully.");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else {
            response.put("message", "Failed to register patient.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    // 5. Patient Login
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Login login) {
        return service.validatePatientLogin(login.getEmail(), login.getPassword());
    }


    // 6. Get Patient Appointments
    @GetMapping("/appointments/{id}/{token}/{user}")
    public ResponseEntity<Map<String, Object>> getPatientAppointment(
            @PathVariable Long id,
            @PathVariable String token,
            @PathVariable String user) {

        Map<String, Object> response = new HashMap<>();

        int tokenValid = service.validateToken(token, user);
        if (tokenValid != 1) {
            response.put("message", "Unauthorized: Invalid or expired token.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        return patientService.getPatientAppointment(id);
    }


    // 7. Filter Patient Appointments
    @GetMapping("/appointments/filter/{condition}/{name}/{token}")
    public ResponseEntity<Map<String, Object>> filterPatientAppointment(
            @PathVariable String condition,
            @PathVariable String name,
            @PathVariable String token) {

        Map<String, Object> response = new HashMap<>();

        int tokenValid = service.validateToken(token, "patient");
        if (tokenValid != 1) {
            response.put("message", "Unauthorized: Invalid or expired token.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        return service.filterPatient(token, condition, name);
    }
}