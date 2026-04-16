package com.project.muttley.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.muttley.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
   Optional<User> findByEmail(String email);

   @Query("SELECT u FROM User u JOIN FETCH u.roles where u.email = :email")
   User findByEmailFetchRoles(@Param("email") String email);
}
