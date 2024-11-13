package com.example.hmsAdmin.dto.requestDto;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UsersRequest {
    private Long userId;
    private String username;
    private String email;
    private String password;
    private String status;
    private String role;
    private boolean findAll;

//    private List<Long> userIds; // List of user IDs for deletion
//    private List<String> usernames; // List of usernames for deletion
}