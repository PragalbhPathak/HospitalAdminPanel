package com.example.hmsAdmin.dto.responseDto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminResponse {
    private Long adminId;
    private String name;
    private String email;
    private String status;
    private String contact;
}
