package com.example.hmsAdmin.repository;

import com.example.hmsAdmin.entity.Admin;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdminRepository extends GenericRepository<Admin,Long>{
    Optional<Admin> findByEmail(String email);
    List<Admin> findByName(String name);

   // Optional<Admin> findAdByEmail(String email);
}
