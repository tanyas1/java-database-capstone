package com.project.back_end.controllers;

import com.project.back_end.models.Prescription;
import com.project.back_end.services.AppointmentService;
import com.project.back_end.services.PrescriptionService;
import com.project.back_end.services.Service;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("${api.path}prescription")
public class PrescriptionController {

    private final PrescriptionService prescriptionService;
    private final Service service;
    private final AppointmentService appointmentService;

    // 2. Constructor Injection
    public PrescriptionController(PrescriptionService prescriptionService,
                                   Service service,
                                   AppointmentService appointmentService) {
        this.prescriptionService = prescriptionService;
        this.service             = service;
        this.appointmentService  = appointmentService;
    }


    // 3. Save Prescription
    @PostMapping("/{token}")
    public ResponseEntity<Map<String, Object>> savePrescription(
            @Valid @RequestBody Prescription prescription,
            @PathVariable String token) {

        Map<String, Object> response = new HashMap<>();

        int tokenValid = service.validateToken(token, "doctor");
        if (tokenValid != 1) {
            response.put("message", "Unauthorized: Invalid or expired token.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        // Update appointment status to indicate prescription has been issued (status = 1)
        appointmentService.changeStatus(1, prescription.getAppointmentId());

        return prescriptionService.savePrescription(prescription);
    }


    // 4. Get Prescription
    @GetMapping("/{appointmentId}/{token}")
    public ResponseEntity<Map<String, Object>> getPrescription(
            @PathVariable Long appointmentId,
            @PathVariable String token) {

        Map<String, Object> response = new HashMap<>();

        int tokenValid = service.validateToken(token, "doctor");
        if (tokenValid != 1) {
            response.put("message", "Unauthorized: Invalid or expired token.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        return prescriptionService.getPrescription(appointmentId);
    }
}