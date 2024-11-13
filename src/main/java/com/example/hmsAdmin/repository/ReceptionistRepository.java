package com.example.hmsAdmin.repository;

import com.example.hmsAdmin.entity.Admin;
import com.example.hmsAdmin.entity.Patient;
import com.example.hmsAdmin.entity.Receptionist;

public interface ReceptionistRepository extends GenericRepository<Receptionist,Long>{
     Receptionist findByUserId(Long userId);
}
