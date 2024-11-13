package com.example.hmsAdmin.implementation;

import com.example.hmsAdmin.dto.requestDto.UsersRequest;
import com.example.hmsAdmin.dto.responseDto.BaseApiResponse;

import java.util.List;

public interface UsersImpl {
    BaseApiResponse createOrUpdateUsers(List<UsersRequest> userRequests);
    BaseApiResponse fetchUsers(Long userId, String username, String email, boolean findAll, String role);
}
