package com.example.hmsAdmin.controller;

import com.example.hmsAdmin.baseConstraints.REST_MAPPING_CONSTRAINT;
import com.example.hmsAdmin.dto.requestDto.UsersRequest;
import com.example.hmsAdmin.dto.responseDto.BaseApiResponse;
import com.example.hmsAdmin.dto.responseDto.UsersResponse;
import com.example.hmsAdmin.implementation.UsersImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.example.hmsAdmin.baseConstraints.REST_MAPPING_CONSTRAINT.BASE_URL;
import static com.example.hmsAdmin.baseConstraints.REST_MAPPING_CONSTRAINT.MESSAGE_NAMES.*;
import static com.example.hmsAdmin.baseConstraints.REST_MAPPING_CONSTRAINT.STATUS_CODES.*;
import static com.example.hmsAdmin.baseConstraints.REST_MAPPING_CONSTRAINT.SUCCESS_CODES.FAILURE;
import static com.example.hmsAdmin.baseConstraints.REST_MAPPING_CONSTRAINT.SUCCESS_CODES.SUCCESS;

@RestController
@RequestMapping(BASE_URL)
public class UsersController {

    @Autowired
    private UsersImpl usersImpl;

    @PostMapping(REST_MAPPING_CONSTRAINT.DEFINE_API.CREATE_OR_UPDATE_USERS)
    public ResponseEntity<BaseApiResponse> createOrUpdate(@Valid @RequestBody List<UsersRequest> userRequests) {
        // Check if userRequests is null or empty
        if (userRequests == null || userRequests.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new BaseApiResponse(BAD_REQUEST, FAILURE, "User request list cannot be empty.", Collections.emptyList()));
        }

        try {
            // Call the service method if validation passes
            BaseApiResponse response = usersImpl.createOrUpdateUsers(userRequests);

            // Handle partial success (HTTP 207 for multi-status)
            if ("Partial Status".equals(response.getStatus())) {
                return ResponseEntity.status(HttpStatus.MULTI_STATUS) // 207 status code
                        .body(response);
            }
            // For full success
            return ResponseEntity.ok(response);

        } catch (Exception e) {

            // Return internal server error response with detailed error message
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BaseApiResponse(INTERNAL_SERVER_ERROR, FAILURE, "An error occurred: " + e.getMessage(), Collections.emptyList()));
        }
    }

//    ------------------------------------------------------------------------------------------------------------------------

    @PostMapping(REST_MAPPING_CONSTRAINT.DEFINE_API.FETCH_USERS)
    public ResponseEntity<BaseApiResponse> fetchUsers(@RequestBody List<UsersRequest> requests) {
        try {
            // Validation checks
            if (requests == null || requests.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new BaseApiResponse(BAD_REQUEST, FAILURE, FIELD_REQUIRED_MESSAGE, Collections.emptyList()));
            }

            List<UsersResponse> allUsers = new ArrayList<>();

            for (UsersRequest request : requests) {
                // Fetch users based on request parameters
                BaseApiResponse response = usersImpl.fetchUsers(
                        request.getUserId(),
                        request.getUsername(),
                        request.getEmail(),
                        request.isFindAll(),
                        request.getRole()
                );

                // Collect user data from response
                if (response.getData() != null) {
                    allUsers.addAll((List<UsersResponse>) response.getData());
                }
            }
            return ResponseEntity.ok(new BaseApiResponse(SUCCESS_OK, SUCCESS, COMMON_MESSAGE_DATA_FETCHED, allUsers));

        } catch (Exception e) {
            // Handle any exceptions that occur
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BaseApiResponse(INTERNAL_SERVER_ERROR, FAILURE, COMMON_ERROR, Collections.emptyList()));
        }
    }

}

