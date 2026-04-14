package com.pbl.digital_healthcare.controller;

import com.pbl.digital_healthcare.models.Appointment;
import com.pbl.digital_healthcare.models.AppointmentStatus;
import com.pbl.digital_healthcare.repository.AppointmentRepository;
import com.pbl.digital_healthcare.repository.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/test")
public class TestController {
    private UserRepository userRepository;
    private AppointmentRepository appointmentRepository;

    public TestController(UserRepository userRepository, AppointmentRepository appointmentRepository){
        this.userRepository = userRepository;
        this.appointmentRepository = appointmentRepository;
    }

    @GetMapping
    public String test(){
        return "Successfull";
    }

    @GetMapping("/appointments/doctor/{doctorId}")
    public String getAppointmentsForDoctor(@PathVariable Long doctorId) {
        try {
            List<Appointment> appointments = appointmentRepository.findAll();
            StringBuilder result = new StringBuilder();
            result.append("Total appointments: ").append(appointments.size()).append("\n");
            
            for (Appointment apt : appointments) {
                if (apt.getDoctor().getId().equals(doctorId)) {
                    result.append("Appointment ID: ").append(apt.getId())
                          .append(", Slot: ").append(apt.getSlot())
                          .append(", Status: ").append(apt.getStatus())
                          .append("\n");
                }
            }
            
            return result.toString();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}
