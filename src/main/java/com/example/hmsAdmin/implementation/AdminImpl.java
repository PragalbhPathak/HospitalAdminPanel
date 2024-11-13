package com.example.hmsAdmin.implementation;

import com.example.hmsAdmin.dto.requestDto.AdminLoginRequest;
import com.example.hmsAdmin.dto.requestDto.AdminRequest;
import com.example.hmsAdmin.dto.responseDto.BaseApiResponse;

public interface AdminImpl {
     BaseApiResponse createOrUpdateAdmin(AdminRequest adminRequest);
     BaseApiResponse fetchAdmins(Long adminId, String name);

     BaseApiResponse loginAdmin(AdminLoginRequest adminLoginRequest);

}
