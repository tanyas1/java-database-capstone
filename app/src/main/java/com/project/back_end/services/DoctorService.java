package com.project.back_end.services;

import com.project.back_end.models.Appointment;
import com.project.back_end.models.Doctor;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.DoctorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;
    private final TokenService tokenService;

    // 2. Constructor Injection
    public DoctorService(DoctorRepository doctorRepository,
                         AppointmentRepository appointmentRepository,
                         TokenService tokenService) {
        this.doctorRepository      = doctorRepository;
        this.appointmentRepository = appointmentRepository;
        this.tokenService          = tokenService;
    }


    // 4. Get Doctor Availability
    @Transactional
    public List<String> getDoctorAvailability(Long doctorId, LocalDate date) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end   = date.atTime(23, 59, 59);

        List<Appointment> booked = appointmentRepository
                .findByDoctorIdAndAppointmentTimeBetween(doctorId, start, end);

        List<String> bookedTimes = booked.stream()
                .map(a -> a.getAppointmentTime().toLocalTime().toString())
                .collect(Collectors.toList());

        Optional<Doctor> doctorOpt = doctorRepository.findById(doctorId);
        if (doctorOpt.isEmpty()) return new ArrayList<>();

        return doctorOpt.get().getAvailableTimes().stream()
                .filter(t -> bookedTimes.stream().noneMatch(bt -> bt.startsWith(t.substring(0, 5))))
                .collect(Collectors.toList());
    }


    // 5. Save Doctor
    public int saveDoctor(Doctor doctor) {
        try {
            Doctor existing = doctorRepository.findByEmail(doctor.getEmail());
            if (existing != null) return -1;

            doctorRepository.save(doctor);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }


    // 6. Update Doctor
    public int updateDoctor(Doctor doctor) {
        try {
            if (!doctorRepository.existsById(doctor.getId())) return -1;

            doctorRepository.save(doctor);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }


    // 7. Get All Doctors
    @Transactional
    public List<Doctor> getDoctors() {
        List<Doctor> doctors = doctorRepository.findAll();
        // Eagerly touch availableTimes to avoid lazy loading issues
        doctors.forEach(d -> d.getAvailableTimes().size());
        return doctors;
    }


    // 8. Delete Doctor
    public int deleteDoctor(Long doctorId) {
        try {
            if (!doctorRepository.existsById(doctorId)) return -1;

            appointmentRepository.deleteAllByDoctorId(doctorId);
            doctorRepository.deleteById(doctorId);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }


    // 9. Validate Doctor Login
    public java.util.Map<String, Object> validateDoctor(String email, String password) {
        java.util.Map<String, Object> response = new java.util.HashMap<>();
        Doctor doctor = doctorRepository.findByEmail(email);

        if (doctor == null) {
            response.put("status", -1);
            response.put("message", "Doctor not found.");
            return response;
        }

        if (!doctor.getPassword().equals(password)) {
            response.put("status", 0);
            response.put("message", "Invalid password.");
            return response;
        }

        String token = tokenService.generateToken(email);
        response.put("status", 1);
        response.put("token", token);
        response.put("message", "Login successful.");
        return response;
    }


    // 10. Find Doctor By Name
    @Transactional
    public List<Doctor> findDoctorByName(String name) {
        List<Doctor> doctors = doctorRepository.findByNameLike(name);
        doctors.forEach(d -> d.getAvailableTimes().size());
        return doctors;
    }


    // 11. Filter By Name, Specialty, and Time
    @Transactional
    public List<Doctor> filterDoctorsByNameSpecilityandTime(String name, String specialty, String time) {
        List<Doctor> doctors = doctorRepository
                .findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(name, specialty);
        return filterDoctorByTime(doctors, time);
    }


    // 12. Filter Doctor List By Time (AM/PM)
    public List<Doctor> filterDoctorByTime(List<Doctor> doctors, String time) {
        return doctors.stream()
                .filter(d -> d.getAvailableTimes().stream()
                        .anyMatch(t -> matchesTimePeriod(t, time)))
                .collect(Collectors.toList());
    }


    // 13. Filter By Name and Time
    @Transactional
    public List<Doctor> filterDoctorByNameAndTime(String name, String time) {
        List<Doctor> doctors = doctorRepository.findByNameLike(name);
        return filterDoctorByTime(doctors, time);
    }


    // 14. Filter By Name and Specialty
    @Transactional
    public List<Doctor> filterDoctorByNameAndSpecility(String name, String specialty) {
        return doctorRepository
                .findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(name, specialty);
    }


    // 15. Filter By Time and Specialty
    @Transactional
    public List<Doctor> filterDoctorByTimeAndSpecility(String specialty, String time) {
        List<Doctor> doctors = doctorRepository.findBySpecialtyIgnoreCase(specialty);
        return filterDoctorByTime(doctors, time);
    }


    // 16. Filter By Specialty
    @Transactional
    public List<Doctor> filterDoctorBySpecility(String specialty) {
        return doctorRepository.findBySpecialtyIgnoreCase(specialty);
    }


    // 17. Filter All Doctors By Time
    @Transactional
    public List<Doctor> filterDoctorsByTime(String time) {
        List<Doctor> all = doctorRepository.findAll();
        return filterDoctorByTime(all, time);
    }


    // Helper: check if a time string matches AM or PM period
    private boolean matchesTimePeriod(String timeSlot, String period) {
        if (timeSlot == null || period == null) return false;
        return timeSlot.toUpperCase().contains(period.toUpperCase());
    }
}