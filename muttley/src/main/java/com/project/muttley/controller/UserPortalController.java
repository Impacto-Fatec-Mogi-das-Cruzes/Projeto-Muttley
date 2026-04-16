package com.project.muttley.controller;

import com.project.muttley.dto.user.ParticipantDashboardDTO;
import com.project.muttley.service.UserPortalService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserPortalController {

    private final UserPortalService userPortalService;

    public UserPortalController(UserPortalService userPortalService) {
        this.userPortalService = userPortalService;
    }

    @GetMapping("/dashboard")
    public ParticipantDashboardDTO dashboard(Authentication authentication) {
        return userPortalService.getDashboard(authentication.getName());
    }
}
