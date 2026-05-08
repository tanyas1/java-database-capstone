package com.project.back_end.controllers;

import com.project.back_end.models.Appointment;
import com.project.back_end.services.AppointmentService;
import com.project.back_end.services.Service;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("${api.path}appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final Service service;

    // 2. Constructor Injection
    public AppointmentController(AppointmentService appointmentService, Service service) {
        this.appointmentService = appointmentService;
        this.service            = service;
    }


    // 3. Get Appointments
    @GetMapping("/{date}/{patientName}/{token}")
    public ResponseEntity<Map<String, Object>> getAppointments(
            @PathVariable String date,
            @PathVariable String patientName,
            @PathVariable String token) {

        Map<String, Object> response = new HashMap<>();

        int tokenValid = service.validateToken(token, "doctor");
        if (tokenValid != 1) {
            response.put("message", "Unauthorized: Invalid or expired token.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        // Extract doctor ID from token and fetch appointments
        LocalDate appointmentDate = LocalDate.parse(date);
        String email = null;
        try {
            email = service.extractEmailFromToken(token);
        } catch (Exception e) {
            response.put("message", "Error extracting token information.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

        // Get doctor by email to retrieve ID
        com.project.back_end.models.Doctor doctor = service.getDoctorByEmail(email);
        if (doctor == null) {
            response.put("message", "Doctor not found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        List<Appointment> appointments = appointmentService
                .getAppointments(doctor.getId(), appointmentDate, patientName);

        response.put("appointments", appointments);
        return ResponseEntity.ok(response);
    }


    // 4. Book Appointment
    @PostMapping("/{token}")
    public ResponseEntity<Map<String, Object>> bookAppointment(
            @Valid @RequestBody Appointment appointment,
            @PathVariable String token) {

        Map<String, Object> response = new HashMap<>();

        int tokenValid = service.validateToken(token, "patient");
        if (tokenValid != 1) {
            response.put("message", "Unauthorized: Invalid or expired token.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        // Validate doctor availability
        int valid = service.validateAppointment(
                appointment.getDoctor().getId(),
                appointment.getAppointmentTime());

        if (valid == -1) {
            response.put("message", "Doctor not found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        if (valid == 0) {
            response.put("message", "The selected time slot is not available.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        int result = appointmentService.bookAppointment(appointment);
        if (result == 1) {
            response.put("message", "Appointment booked successfully.");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else {
            response.put("message", "Failed to book appointment.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    // 5. Update Appointment
    @PutMapping("/{token}")
    public ResponseEntity<Map<String, Object>> updateAppointment(
            @Valid @RequestBody Appointment appointment,
            @PathVariable String token) {

        Map<String, Object> response = new HashMap<>();

        int tokenValid = service.validateToken(token, "patient");
        if (tokenValid != 1) {
            response.put("message", "Unauthorized: Invalid or expired token.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        // Extract patient ID from token
        String email = service.extractEmailFromToken(token);
        com.project.back_end.models.Patient patient = service.getPatientByEmail(email);
        if (patient == null) {
            response.put("message", "Patient not found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        String result = appointmentService.updateAppointment(appointment, patient.getId());
        response.put("message", result);

        if (result.contains("successfully")) {
            return ResponseEntity.ok(response);
        } else if (result.contains("not found")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } else if (result.contains("Unauthorized")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    // 6. Cancel Appointment
    @DeleteMapping("/{appointmentId}/{token}")
    public ResponseEntity<Map<String, Object>> cancelAppointment(
            @PathVariable Long appointmentId,
            @PathVariable String token) {

        Map<String, Object> response = new HashMap<>();

        int tokenValid = service.validateToken(token, "patient");
        if (tokenValid != 1) {
            response.put("message", "Unauthorized: Invalid or expired token.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        // Extract patient ID from token
        String email = service.extractEmailFromToken(token);
        com.project.back_end.models.Patient patient = service.getPatientByEmail(email);
        if (patient == null) {
            response.put("message", "Patient not found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        String result = appointmentService.cancelAppointment(appointmentId, patient.getId());
        response.put("message", result);

        if (result.contains("successfully")) {
            return ResponseEntity.ok(response);
        } else if (result.contains("not found")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } else if (result.contains("Unauthorized")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}