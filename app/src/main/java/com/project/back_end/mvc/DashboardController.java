package com.project.back_end.mvc;

import com.project.back_end.services.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class DashboardController {

    // 2. Autowire the Shared Service
    @Autowired
    private Service service;


    // 3. Admin Dashboard
    @GetMapping("/adminDashboard/{token}")
    public String adminDashboard(@PathVariable String token) {

        // Validate token for admin role
        int result = service.validateToken(token, "admin");

        if (result == 1) {
            return "admin/adminDashboard";
        } else {
            return "redirect:/";
        }
    }


    // 4. Doctor Dashboard
    @GetMapping("/doctorDashboard/{token}")
    public String doctorDashboard(@PathVariable String token) {

        // Validate token for doctor role
        int result = service.validateToken(token, "doctor");

        if (result == 1) {
            return "doctor/doctorDashboard";
        } else {
            return "redirect:/";
        }
    }
}