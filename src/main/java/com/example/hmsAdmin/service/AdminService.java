package com.example.hmsAdmin.service;

import com.example.hmsAdmin.dto.requestDto.AdminLoginRequest;
import com.example.hmsAdmin.dto.requestDto.AdminRequest;
import com.example.hmsAdmin.dto.responseDto.AdminResponse;
import com.example.hmsAdmin.dto.responseDto.BaseApiResponse;
import com.example.hmsAdmin.entity.Admin;
import com.example.hmsAdmin.implementation.AdminImpl;
import com.example.hmsAdmin.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.hmsAdmin.baseConstraints.REST_MAPPING_CONSTRAINT.MESSAGE_NAMES.*;
import static com.example.hmsAdmin.baseConstraints.REST_MAPPING_CONSTRAINT.STATUS_CODES.*;
import static com.example.hmsAdmin.baseConstraints.REST_MAPPING_CONSTRAINT.SUCCESS_CODES.FAILURE;
import static com.example.hmsAdmin.baseConstraints.REST_MAPPING_CONSTRAINT.SUCCESS_CODES.SUCCESS;

@Service
public class AdminService implements AdminImpl {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;
//----------------------------------------------------------------------------------------------------------------

    // Method to create Admin if not created previously or update Admin if it is already created.

    public BaseApiResponse createOrUpdateAdmin(AdminRequest requestDto) {
        Admin admin;
        try {
            if (requestDto.getAdminId() != null) {  // Check if adminId is provided
                // Find existing admin
                Optional<Admin> existingAdminOpt = adminRepository.findById(requestDto.getAdminId());
                if (existingAdminOpt.isPresent()) {
                    admin = existingAdminOpt.get();
                    // Update fields
                    admin.setName(requestDto.getName());
                    admin.setEmail(requestDto.getEmail());
                    admin.setPassword(passwordEncoder.encode(requestDto.getPassword()));
                    admin.setStatus(requestDto.getStatus());
                    admin.setContact(requestDto.getContact());

                    // Save updated admin
                    Admin savedAdmin = adminRepository.save(admin);

                    return new BaseApiResponse(SUCCESS_OK, SUCCESS, COMMON_MESSAGE_UPDATION, savedAdmin);
                } else {
                    return new BaseApiResponse(NOT_FOUND, FAILURE, NOT_PRESENT, Collections.emptyList());
                }
            } else {
                // Create new admin (only if adminId is null)
                admin = new Admin();
                admin.setName(requestDto.getName());
                admin.setEmail(requestDto.getEmail());
                admin.setPassword(passwordEncoder.encode(requestDto.getPassword()));
                admin.setStatus(requestDto.getStatus());
                admin.setContact(requestDto.getContact());

                // Save (create new admin)
                Admin savedAdmin = adminRepository.save(admin);

                AdminResponse responseDto = new AdminResponse(
                        savedAdmin.getAdminId(),
                        savedAdmin.getName(),
                        savedAdmin.getEmail(),
                        savedAdmin.getStatus(),
                        savedAdmin.getContact()
                );

                return new BaseApiResponse(SUCCESS_OK, SUCCESS, COMMON_MESSAGE_CREATION, responseDto);
            }
        } catch (Exception e) {
            // Exception handling
            return new BaseApiResponse(INTERNAL_SERVER_ERROR, FAILURE, COMMON_ERROR, Collections.emptyList());
        }
    }

//    --------------------------------------------------------------------------------------------------------------

    // Method to get all Admins or get admin by name or id

    public BaseApiResponse fetchAdmins(Long adminId, String name) {
        if (adminId != null) {
            // Fetch a single admin by ID
            Optional<Admin> adminOptional = adminRepository.findById(adminId);
            if (adminOptional.isPresent()) {
                Admin admin = adminOptional.get();
                AdminResponse responseDto = new AdminResponse(
                        admin.getAdminId(),
                        admin.getName(),
                        admin.getEmail(),
                        admin.getStatus(),
                        admin.getContact()
                );
                return new BaseApiResponse(SUCCESS_OK, SUCCESS, COMMON_MESSAGE_DATA_FETCHED, responseDto);
            } else {
                return new BaseApiResponse(BAD_REQUEST, FAILURE, NOT_PRESENT, Collections.emptyList());
            }
        } else if (name != null) {
            // Fetch a single admin by name
            List<Admin> adminsByName = adminRepository.findByName(name);
            if (!adminsByName.isEmpty()) {
                List<AdminResponse> responseDto = adminsByName.stream()
                        .map(admin -> new AdminResponse(
                                admin.getAdminId(),
                                admin.getName(),
                                admin.getEmail(),
                                admin.getStatus(),
                                admin.getContact()))
                        .collect(Collectors.toList());
                return new BaseApiResponse(SUCCESS_OK, SUCCESS, COMMON_MESSAGE_DATA_FETCHED, responseDto);
            } else {
                return new BaseApiResponse(BAD_REQUEST, FAILURE, NOT_PRESENT, Collections.emptyList());
            }
        } else {
            // Fetch all admins if neither parameter is provided
            List<Admin> admins = adminRepository.findAll();
            List<AdminResponse> responseDto = admins.stream()
                    .map(admin -> new AdminResponse(
                            admin.getAdminId(),
                            admin.getName(),
                            admin.getEmail(),
                            admin.getStatus(),
                            admin.getContact()))
                    .collect(Collectors.toList());

            return new BaseApiResponse(SUCCESS_OK, SUCCESS, COMMON_MESSAGE_DATA_FETCHED, responseDto);
        }
    }
//    ----------------------------------------------------------------------------------------------------------

    // Method to Login Admin

    @Override
    public BaseApiResponse loginAdmin(AdminLoginRequest adminLoginRequest) {
        BaseApiResponse response = new BaseApiResponse();

        // Check if admin exists by email
        Optional<Admin> existingAdminOpt = adminRepository.findByEmail(adminLoginRequest.getEmail());

        if (existingAdminOpt.isEmpty()) {
            response.setSuccess(0);
            response.setMessage("Admin not found");
            return response;
        }

        Admin admin = existingAdminOpt.get();

        // Check if the provided password matches the stored password
        if (passwordEncoder.matches(adminLoginRequest.getPassword(), admin.getPassword())) {
            // Generate JWT token
            String token = jwtService.generateToken(admin.getEmail());

            response.setSuccess(1);
            response.setMessage("Login successful");
            response.setData(Collections.singletonMap("token", token)); // Send token in response
        } else {
            response.setSuccess(0);
            response.setMessage("Invalid password");
        }
        return response;
    }

}
