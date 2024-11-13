package com.example.hmsAdmin.controller;

import com.example.hmsAdmin.baseConstraints.REST_MAPPING_CONSTRAINT;
import com.example.hmsAdmin.dto.requestDto.AdminLoginRequest;
import com.example.hmsAdmin.dto.requestDto.AdminRequest;
import com.example.hmsAdmin.dto.requestDto.SearchAdminRequest;
import com.example.hmsAdmin.dto.responseDto.BaseApiResponse;
import com.example.hmsAdmin.implementation.AdminImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

import static com.example.hmsAdmin.baseConstraints.REST_MAPPING_CONSTRAINT.BASE_URL;
import static com.example.hmsAdmin.baseConstraints.REST_MAPPING_CONSTRAINT.MESSAGE_NAMES.*;
import static com.example.hmsAdmin.baseConstraints.REST_MAPPING_CONSTRAINT.STATUS_CODES.*;
import static com.example.hmsAdmin.baseConstraints.REST_MAPPING_CONSTRAINT.SUCCESS_CODES.FAILURE;


@RestController
@RequestMapping(BASE_URL)
public class AdminController {

    @Autowired
    private AdminImpl adminImpl;

    // -----------------------------------------------------------------------------------------------

    @PostMapping(REST_MAPPING_CONSTRAINT.DEFINE_API.CREATE_OR_UPDATE_ADMIN)
    public ResponseEntity<BaseApiResponse> createOrUpdate(@RequestBody AdminRequest request) {
        try {
            // Validation checks
            if (request.getName() == null || request.getName().isEmpty()) {
                BaseApiResponse errorResponse = new BaseApiResponse(BAD_REQUEST, FAILURE, FIELD_REQUIRED_MESSAGE, Collections.emptyList());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }

            if (request.getEmail() == null || request.getEmail().isEmpty()) {
                BaseApiResponse errorResponse = new BaseApiResponse(BAD_REQUEST, FAILURE, FIELD_REQUIRED_MESSAGE, Collections.emptyList());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }

            if (request.getPassword() == null || request.getPassword().isEmpty()) {
                BaseApiResponse errorResponse = new BaseApiResponse(BAD_REQUEST, FAILURE, FIELD_REQUIRED_MESSAGE, Collections.emptyList());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }
            if (request.getContact() == null || !request.getContact().matches("^[0-9]{10,12}$")) {
                return ResponseEntity.badRequest()
                        .body(new BaseApiResponse(BAD_REQUEST, FAILURE, FIELD_REQUIRED_MESSAGE, Collections.emptyList()));
            }

            // Further validations can be added here (e.g., email format, password strength)

            BaseApiResponse baseApiResponse = adminImpl.createOrUpdateAdmin(request);
            if (baseApiResponse.getSuccess() == 1) {
                if (request.getAdminId() == null || request.getAdminId() == 0) {
                    return ResponseEntity.status(HttpStatus.CREATED).body(baseApiResponse);
                } else {
                    return ResponseEntity.status(HttpStatus.ACCEPTED).body(baseApiResponse);
                }
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseApiResponse);
            }
        } catch (RuntimeException e) {
            BaseApiResponse errorResponse = new BaseApiResponse(INTERNAL_SERVER_ERROR, FAILURE, COMMON_ERROR, Collections.emptyList());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

//    ------------------------------------------------------------------------------------------------

    @PostMapping(REST_MAPPING_CONSTRAINT.DEFINE_API.FETCH_ADMINS)
    public ResponseEntity<BaseApiResponse> fetchAdmins(@RequestBody SearchAdminRequest searchAdminRequest) {
        try {
            Long adminId = searchAdminRequest.getAdminId();  // Extract adminId from the request DTO
            String name = searchAdminRequest.getName();

            // Validation checks
            if (adminId != null && adminId <= 0) {
                BaseApiResponse errorResponse = new BaseApiResponse(BAD_REQUEST, FAILURE, COMMON_MESSAGE_INVALID, Collections.emptyList());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }

            if (name != null && name.trim().isEmpty()) {
                BaseApiResponse errorResponse = new BaseApiResponse(BAD_REQUEST, FAILURE, COMMON_MESSAGE_INVALID, Collections.emptyList());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }

            BaseApiResponse response = adminImpl.fetchAdmins(adminId, name);  // Call the service method
            return ResponseEntity.ok(response);  // Return the response wrapped in ResponseEntity

        } catch (Exception e) {
            System.out.println("Error occurred at the time of fetching");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BaseApiResponse(INTERNAL_SERVER_ERROR, FAILURE, COMMON_ERROR, Collections.emptyList()));
        }
    }


//    --------------------------------------------------------------------------------------------------

    @PostMapping(REST_MAPPING_CONSTRAINT.DEFINE_API.LOGIN_ADMIN)
    public ResponseEntity<BaseApiResponse> loginAdmin(@RequestBody AdminLoginRequest adminLoginRequest) {
        try {
            if (adminLoginRequest.getEmail() == null || adminLoginRequest.getPassword() == null) {
                // Return bad request if email or password is missing
                BaseApiResponse errorResponse = new BaseApiResponse(BAD_REQUEST,FAILURE, "Email and password are required", Collections.emptyList());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }
            BaseApiResponse response = adminImpl.loginAdmin(adminLoginRequest);

            // Check if the login was successful
            if (response.getSuccess() == 1) {
                response.setStatus(String.valueOf(HttpStatus.OK.value()));  // Set HTTP status code as string
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
                response.setStatus(String.valueOf(HttpStatus.UNAUTHORIZED.value()));  // Unauthorized status
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

        } catch (Exception e) {
            BaseApiResponse errorResponse = new BaseApiResponse(INTERNAL_SERVER_ERROR, FAILURE, "An error occurred", Collections.emptyList());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

}