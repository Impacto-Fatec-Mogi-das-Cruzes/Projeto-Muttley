package com.project.muttley.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.muttley.repository.EventRepository;

@Service
public class EventService {
    
    @Autowired EventRepository eventRepository;

}
