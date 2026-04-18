package com.project.muttley.repository;

import com.project.muttley.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u JOIN FETCH u.roles WHERE u.email = :email")
    User findByEmailFetchRoles(@Param("email") String email);

    List<User> findByEmailContainingIgnoreCase(String email);

    List<User> findByRoles_Name(String role);

    List<User> findByEmailContainingIgnoreCaseAndRoles_Name(String email, String role);

    Page<User> findByEmailContainingIgnoreCase(String email, Pageable pageable);

    boolean existsByEmail(String email);
}