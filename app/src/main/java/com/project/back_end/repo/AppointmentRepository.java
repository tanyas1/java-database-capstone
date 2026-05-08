package com.project.back_end.repo;

import com.project.back_end.models.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    // Find appointments for a doctor within a time range (eagerly fetch available times)
    @Query("SELECT a FROM Appointment a LEFT JOIN FETCH a.doctor.availableTimes " +
           "WHERE a.doctor.id = :doctorId " +
           "AND a.appointmentTime BETWEEN :start AND :end")
    List<Appointment> findByDoctorIdAndAppointmentTimeBetween(
            @Param("doctorId") Long doctorId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);


    // Find appointments for a doctor filtered by patient name within a time range
    @Query("SELECT a FROM Appointment a LEFT JOIN FETCH a.doctor LEFT JOIN FETCH a.patient " +
           "WHERE a.doctor.id = :doctorId " +
           "AND LOWER(a.patient.name) LIKE LOWER(CONCAT('%', :patientName, '%')) " +
           "AND a.appointmentTime BETWEEN :start AND :end")
    List<Appointment> findByDoctorIdAndPatient_NameContainingIgnoreCaseAndAppointmentTimeBetween(
            @Param("doctorId") Long doctorId,
            @Param("patientName") String patientName,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);


    // Delete all appointments for a specific doctor
    @Modifying
    @Transactional
    @Query("DELETE FROM Appointment a WHERE a.doctor.id = :doctorId")
    void deleteAllByDoctorId(@Param("doctorId") Long doctorId);


    // Find all appointments for a specific patient
    List<Appointment> findByPatientId(Long patientId);


    // Find appointments for a patient by status, ordered by appointment time ascending
    List<Appointment> findByPatient_IdAndStatusOrderByAppointmentTimeAsc(Long patientId, int status);


    // Filter appointments by doctor name (LIKE) and patient ID
    @Query("SELECT a FROM Appointment a " +
           "WHERE a.doctor.name LIKE %:doctorName% " +
           "AND a.patient.id = :patientId")
    List<Appointment> filterByDoctorNameAndPatientId(
            @Param("doctorName") String doctorName,
            @Param("patientId") Long patientId);


    // Filter appointments by doctor name, patient ID, and status
    @Query("SELECT a FROM Appointment a " +
           "WHERE a.doctor.name LIKE %:doctorName% " +
           "AND a.patient.id = :patientId " +
           "AND a.status = :status")
    List<Appointment> filterByDoctorNameAndPatientIdAndStatus(
            @Param("doctorName") String doctorName,
            @Param("patientId") Long patientId,
            @Param("status") int status);


    // Update the status of a specific appointment
    @Modifying
    @Transactional
    @Query("UPDATE Appointment a SET a.status = :status WHERE a.id = :id")
    void updateStatus(@Param("status") int status, @Param("id") long id);
}