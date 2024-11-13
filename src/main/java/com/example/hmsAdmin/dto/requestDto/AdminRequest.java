package com.example.hmsAdmin.dto.requestDto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminRequest {
    @NotNull(message = "Admin ID cannot be null")
    private Long adminId;
    private String name;
    private String email;
    private String password;
    private String status;
    private String contact;
}
