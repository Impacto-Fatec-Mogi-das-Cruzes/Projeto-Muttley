package com.project.muttley.repository;

import com.project.muttley.model.Medal;
import com.project.muttley.model.Event;
import com.project.muttley.model.enums.RoleInEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedalRepository extends JpaRepository<Medal, Long> {
    List<Medal> findByEventAndTargetRoleAndActiveTrue(Event event, RoleInEvent targetRole);
}
