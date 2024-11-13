package com.example.hmsAdmin.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Data
@Entity
@Table(name = "Patient")
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long patientId;

    @NotBlank(message = "name can't be blank")
    @Column(name = "name",nullable = false)
    private String name;

    @NotNull
    @Column(name = "age")
    private int age;

    @Column(name = "gender")
    private String gender;

    @Column(name = "status")
    private String status;

    @Column(name = "address")
    private String address;

    @Column(name = "medicalHistory")
    private String medicalHistory;

    @Column(name = "contact")
    private String contact;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email can't be empty")
    @Column(name = "email",nullable = false,unique = true)
    private String email;

    @NotBlank(message = "Password can't be null or empty")
    @Size(min = 6,message = "Password should have atleast 6 characters")
    @Column(name = "password",nullable = false)
    private String password;

    @Column(name = "userId")
    private Long userId;
    @Column(name = "doctorId")
    private Long doctorId;
    @Column(name = "nurseId")
    private Long nurseId;

}
