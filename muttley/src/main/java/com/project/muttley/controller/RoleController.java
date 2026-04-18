package com.project.muttley.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.muttley.model.Role;
import com.project.muttley.service.RoleService;

@RestController
@RequestMapping("users/roles")
public class RoleController {

    @Autowired RoleService roleService;

    @GetMapping
    List<Role> getRoles() {
        return this.roleService.getRoles();
    }

}
