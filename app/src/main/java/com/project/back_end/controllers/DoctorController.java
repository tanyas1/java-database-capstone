package com.project.back_end.controllers;

import com.project.back_end.DTO.Login;
import com.project.back_end.models.Doctor;
import com.project.back_end.services.DoctorService;
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
@RequestMapping("${api.path}doctor")
public class DoctorController {

    private final DoctorService doctorService;
    private final Service service;

    // 2. Constructor Injection
    public DoctorController(DoctorService doctorService, Service service) {
        this.doctorService = doctorService;
        this.service       = service;
    }


    // 3. Get Doctor Availability
    @GetMapping("/availability/{user}/{doctorId}/{date}/{token}")
    public ResponseEntity<Map<String, Object>> getDoctorAvailability(
            @PathVariable String user,
            @PathVariable Long doctorId,
            @PathVariable String date,
            @PathVariable String token) {

        Map<String, Object> response = new HashMap<>();

        int tokenValid = service.validateToken(token, user);
        if (tokenValid != 1) {
            response.put("message", "Unauthorized: Invalid or expired token.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        List<String> availability = doctorService.getDoctorAvailability(doctorId, LocalDate.parse(date));
        response.put("availableTimes", availability);
        return ResponseEntity.ok(response);
    }


    // 4. Get All Doctors
    @GetMapping
    public ResponseEntity<Map<String, Object>> getDoctor() {
        Map<String, Object> response = new HashMap<>();
        List<Doctor> doctors = doctorService.getDoctors();
        response.put("doctors", doctors);
        return ResponseEntity.ok(response);
    }


    // 5. Save Doctor
    @PostMapping("/{token}")
    public ResponseEntity<Map<String, Object>> saveDoctor(
            @Valid @RequestBody Doctor doctor,
            @PathVariable String token) {

        Map<String, Object> response = new HashMap<>();

        int tokenValid = service.validateToken(token, "admin");
        if (tokenValid != 1) {
            response.put("message", "Unauthorized: Invalid or expired token.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        int result = doctorService.saveDoctor(doctor);

        if (result == -1) {
            response.put("message", "Doctor with this email already exists.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        } else if (result == 1) {
            response.put("message", "Doctor added successfully.");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else {
            response.put("message", "Failed to add doctor.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    // 6. Doctor Login
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> doctorLogin(@Valid @RequestBody Login login) {
        Map<String, Object> result = doctorService.validateDoctor(login.getEmail(), login.getPassword());

        int status = (int) result.get("status");
        if (status == 1) {
            return ResponseEntity.ok(result);
        } else if (status == -1) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
        }
    }


    // 7. Update Doctor
    @PutMapping("/{token}")
    public ResponseEntity<Map<String, Object>> updateDoctor(
            @Valid @RequestBody Doctor doctor,
            @PathVariable String token) {

        Map<String, Object> response = new HashMap<>();

        int tokenValid = service.validateToken(token, "admin");
        if (tokenValid != 1) {
            response.put("message", "Unauthorized: Invalid or expired token.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        int result = doctorService.updateDoctor(doctor);

        if (result == -1) {
            response.put("message", "Doctor not found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } else if (result == 1) {
            response.put("message", "Doctor updated successfully.");
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "Failed to update doctor.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    // 8. Delete Doctor
    @DeleteMapping("/{doctorId}/{token}")
    public ResponseEntity<Map<String, Object>> deleteDoctor(
            @PathVariable Long doctorId,
            @PathVariable String token) {

        Map<String, Object> response = new HashMap<>();

        int tokenValid = service.validateToken(token, "admin");
        if (tokenValid != 1) {
            response.put("message", "Unauthorized: Invalid or expired token.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        int result = doctorService.deleteDoctor(doctorId);

        if (result == -1) {
            response.put("message", "Doctor not found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } else if (result == 1) {
            response.put("message", "Doctor deleted successfully.");
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "Failed to delete doctor.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    // 9. Filter Doctors
    @GetMapping("/filter/{name}/{time}/{speciality}")
    public ResponseEntity<Map<String, Object>> filter(
            @PathVariable String name,
            @PathVariable String time,
            @PathVariable String speciality) {

        return service.filterDoctor(name, time, speciality);
    }
}