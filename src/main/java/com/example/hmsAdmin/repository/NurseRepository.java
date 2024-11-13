package com.example.hmsAdmin.repository;

import com.example.hmsAdmin.entity.Nurse;
import com.example.hmsAdmin.entity.Patient;
import org.springframework.stereotype.Repository;

@Repository
public interface NurseRepository extends GenericRepository<Nurse,Long>{
    Nurse findByUserId(Long userId);
}
