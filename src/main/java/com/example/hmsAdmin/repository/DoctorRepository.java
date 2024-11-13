package com.example.hmsAdmin.repository;

import com.example.hmsAdmin.entity.Doctor;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorRepository extends GenericRepository<Doctor,Long>{
    Doctor findByUserId(Long userId);

}
