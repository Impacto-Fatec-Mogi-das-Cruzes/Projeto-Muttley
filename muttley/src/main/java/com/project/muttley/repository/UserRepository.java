package com.project.muttley.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.muttley.model.User;

public interface UserRepository extends JpaRepository<User, Long>{

    User findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);

}
