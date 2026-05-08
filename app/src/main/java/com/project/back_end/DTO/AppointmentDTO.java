package com.project.back_end.DTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class AppointmentDTO {

    // 1. Unique identifier for the appointment
    private Long id;

    // 2. Doctor's ID
    private Long doctorId;

    // 3. Doctor's name
    private String doctorName;

    // 4. Patient's ID
    private Long patientId;

    // 5. Patient's name
    private String patientName;

    // 6. Patient's email
    private String patientEmail;

    // 7. Patient's phone number
    private String patientPhone;

    // 8. Patient's address
    private String patientAddress;

    // 9. Scheduled date and time of the appointment
    private LocalDateTime appointmentTime;

    // 10. Status: 0 = Scheduled, 1 = Completed, etc.
    private int status;

    // 11. Derived: date part only
    private LocalDate appointmentDate;

    // 12. Derived: time part only
    private LocalTime appointmentTimeOnly;

    // 13. Derived: end time (appointmentTime + 1 hour)
    private LocalDateTime endTime;


    // 14. Constructor
    public AppointmentDTO(Long id,
                          Long doctorId,
                          String doctorName,
                          Long patientId,
                          String patientName,
                          String patientEmail,
                          String patientPhone,
                          String patientAddress,
                          LocalDateTime appointmentTime,
                          int status) {
        this.id               = id;
        this.doctorId         = doctorId;
        this.doctorName       = doctorName;
        this.patientId        = patientId;
        this.patientName      = patientName;
        this.patientEmail     = patientEmail;
        this.patientPhone     = patientPhone;
        this.patientAddress   = patientAddress;
        this.appointmentTime  = appointmentTime;
        this.status           = status;

        // Derived fields calculated from appointmentTime
        this.appointmentDate      = appointmentTime.toLocalDate();
        this.appointmentTimeOnly  = appointmentTime.toLocalTime();
        this.endTime              = appointmentTime.plusHours(1);
    }


    // 15. Getters
    public Long getId()                        { return id; }
    public Long getDoctorId()                  { return doctorId; }
    public String getDoctorName()              { return doctorName; }
    public Long getPatientId()                 { return patientId; }
    public String getPatientName()             { return patientName; }
    public String getPatientEmail()            { return patientEmail; }
    public String getPatientPhone()            { return patientPhone; }
    public String getPatientAddress()          { return patientAddress; }
    public LocalDateTime getAppointmentTime()  { return appointmentTime; }
    public int getStatus()                     { return status; }
    public LocalDate getAppointmentDate()      { return appointmentDate; }
    public LocalTime getAppointmentTimeOnly()  { return appointmentTimeOnly; }
    public LocalDateTime getEndTime()          { return endTime; }
}