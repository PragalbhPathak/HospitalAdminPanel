package com.example.hmsAdmin.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Data
@Entity
@Table(name = "Receptionist")
public class Receptionist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long receptionistId;

    @NotBlank(message = "name can't be blank")
    @Column(name = "name",nullable = false)
    private String name;

    @Column(name = "age")
    private int age;

    @Column(name = "gender")
    private String gender;

    @Column(name = "status")
    private String status;

    @Column(name = "shift")
    private String shift;

    @Column(name = "address")
    private String address;

    @Column(name = "contact")
    private String contact;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email can't be empty")
    @Column(name = "email",unique = true)
    private String email;

    @NotBlank(message = "Password can't be null or empty")
    @Size(min = 6,message = "Password should have atLeast 6 characters")
    @Column(name = "password")
    private String password;

    @Column(name = "userId")
    private Long userId;

}
