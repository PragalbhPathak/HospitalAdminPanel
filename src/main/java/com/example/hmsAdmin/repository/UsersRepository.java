package com.example.hmsAdmin.repository;

import com.example.hmsAdmin.entity.Users;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsersRepository extends GenericRepository<Users,Long>{

    Users findByEmail(String email);
    Users findByUsername(String username);
    Users findByRole(String role);

  //  List<Users> findByUserId(List<Long> userIds);
    List<Users> findByEmailIn(List<String> emails);
    List<Users> findByUsernameIn(List<String> usernames);
    List<Users> findByRoleIn(List<String> roles);

    //Collection<? extends Users> findByIdIn(List<Long> userIds);
}


