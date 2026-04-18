package com.project.muttley.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.muttley.model.Role;
import com.project.muttley.repository.RoleRepository;

@Service
public class RoleService {

    @Autowired RoleRepository roleRepository;

    public List<Role> getRoles() {
        return this.roleRepository.findAll();
    }

}
