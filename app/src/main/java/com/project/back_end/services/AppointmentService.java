package com.project.back_end.services;

import com.project.back_end.models.Appointment;
import com.project.back_end.models.Doctor;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.PatientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final com.project.back_end.services.Service sharedService;
    private final TokenService tokenService;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    // 2. Constructor Injection
    public AppointmentService(AppointmentRepository appointmentRepository,
                              com.project.back_end.services.Service sharedService,
                              TokenService tokenService,
                              PatientRepository patientRepository,
                              DoctorRepository doctorRepository) {
        this.appointmentRepository = appointmentRepository;
        this.sharedService         = sharedService;
        this.tokenService          = tokenService;
        this.patientRepository     = patientRepository;
        this.doctorRepository      = doctorRepository;
    }


    // 4. Book Appointment
    @Transactional
    public int bookAppointment(Appointment appointment) {
        try {
            appointmentRepository.save(appointment);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }


    // 5. Update Appointment
    @Transactional
    public String updateAppointment(Appointment appointment, Long patientId) {
        try {
            Optional<Appointment> existing = appointmentRepository.findById(appointment.getId());

            if (existing.isEmpty()) {
                return "Appointment not found.";
            }

            Appointment current = existing.get();

            // Validate patient ownership
            if (!current.getPatient().getId().equals(patientId)) {
                return "Unauthorized: Patient ID does not match.";
            }

            // Check doctor availability at the new time
            Doctor doctor = doctorRepository.findById(appointment.getDoctor().getId()).orElse(null);
            if (doctor == null) {
                return "Doctor not found.";
            }

            LocalDateTime newTime = appointment.getAppointmentTime();
            boolean available = doctor.getAvailableTimes().stream()
                    .anyMatch(t -> newTime.toLocalTime().getHour() == parseHour(t));

            if (!available) {
                return "Doctor is not available at the requested time.";
            }

            current.setAppointmentTime(newTime);
            current.setDoctor(doctor);
            appointmentRepository.save(current);
            return "Appointment updated successfully.";

        } catch (Exception e) {
            return "Error updating appointment: " + e.getMessage();
        }
    }


    // 6. Cancel Appointment
    @Transactional
    public String cancelAppointment(Long appointmentId, Long patientId) {
        try {
            Optional<Appointment> existing = appointmentRepository.findById(appointmentId);

            if (existing.isEmpty()) {
                return "Appointment not found.";
            }

            Appointment appointment = existing.get();

            // Validate patient ownership
            if (!appointment.getPatient().getId().equals(patientId)) {
                return "Unauthorized: Patient ID does not match.";
            }

            appointmentRepository.delete(appointment);
            return "Appointment cancelled successfully.";

        } catch (Exception e) {
            return "Error cancelling appointment: " + e.getMessage();
        }
    }


    // 7. Get Appointments for a Doctor on a specific day
    @Transactional
    public List<Appointment> getAppointments(Long doctorId, LocalDate date, String patientName) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end   = date.atTime(23, 59, 59);

        if (patientName == null || patientName.equals("null") || patientName.isEmpty()) {
            return appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(doctorId, start, end);
        } else {
            return appointmentRepository
                    .findByDoctorIdAndPatient_NameContainingIgnoreCaseAndAppointmentTimeBetween(
                            doctorId, patientName, start, end);
        }
    }


    // 8. Change Appointment Status
    @Transactional
    public void changeStatus(int status, long appointmentId) {
        appointmentRepository.updateStatus(status, appointmentId);
    }


    // Helper: parse hour from time string (e.g. "09:00 AM")
    private int parseHour(String time) {
        try {
            String[] parts = time.split(":");
            int hour = Integer.parseInt(parts[0].trim());
            if (time.toUpperCase().contains("PM") && hour != 12) hour += 12;
            if (time.toUpperCase().contains("AM") && hour == 12) hour = 0;
            return hour;
        } catch (Exception e) {
            return -1;
        }
    }
}