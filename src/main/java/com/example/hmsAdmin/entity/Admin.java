package com.example.hmsAdmin.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data
@Entity
@Table(name = "Admin")
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long adminId;
    @NotBlank(message = "name can't be blank")
    @Column(name = "name",nullable = false)
    private String name;
    //private int age;
    //private String gender;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email can't be empty")
    @Column(name = "email",nullable = false,unique = true)
    private String email;

    @NotBlank(message = "Password can't be null or empty")
    @Size(min = 6,message = "Password should have atleast 6 characters")
    @Column(name = "password",nullable = false)
    private String password;

    @Column(name = "status")
    private String status;
    //private Date dob;

    @Pattern(regexp = "^[0-9]{10,12}$", message = "Phone number must be a valid phone number with 10 to 12 digits")
    @Column(name = "contact")
    private String contact;
    //private String address;
}

