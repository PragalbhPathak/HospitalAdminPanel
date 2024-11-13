package com.example.hmsAdmin.dto.requestDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchAdminRequest {
    private Long adminId;
    private String name;
}
