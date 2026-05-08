package com.project.back_end.services;

import com.project.back_end.repo.AdminRepository;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.PatientRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class TokenService {

    @Value("${jwt.secret}")
    private String jwtSecret;

    private final AdminRepository adminRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    // 2. Constructor Injection
    public TokenService(AdminRepository adminRepository,
                        DoctorRepository doctorRepository,
                        PatientRepository patientRepository) {
        this.adminRepository   = adminRepository;
        this.doctorRepository  = doctorRepository;
        this.patientRepository = patientRepository;
    }


    // 3. Get Signing Key
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }


    // 4. Generate Token
    public String generateToken(String email) {
        long sevenDaysMillis = 7L * 24 * 60 * 60 * 1000;

        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + sevenDaysMillis))
                .signWith(getSigningKey())
                .compact();
    }


    // 5. Extract Email from Token
    public String extractEmail(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }


    // 6. Validate Token for a Specific Role
    public boolean validateToken(String token, String role) {
        try {
            String email = extractEmail(token);

            switch (role.toLowerCase()) {
                case "admin":
                    return adminRepository.findByUsername(email) != null;
                case "doctor":
                    return doctorRepository.findByEmail(email) != null;
                case "patient":
                    return patientRepository.findByEmail(email) != null;
                default:
                    return false;
            }

        } catch (Exception e) {
            System.err.println("Token validation error: " + e.getMessage());
            return false;
        }
    }
}