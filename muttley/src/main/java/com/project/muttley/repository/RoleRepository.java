package com.project.muttley.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.muttley.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}