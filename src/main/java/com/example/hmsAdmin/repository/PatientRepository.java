package com.example.hmsAdmin.repository;

import com.example.hmsAdmin.entity.Patient;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientRepository extends GenericRepository<Patient,Long>{
    // Find a patient by their user ID
    Patient findByUserId(Long userId);

}
