package com.project.muttley.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.muttley.model.Event;

public interface EventRepository extends JpaRepository<Event, Long>{

}
