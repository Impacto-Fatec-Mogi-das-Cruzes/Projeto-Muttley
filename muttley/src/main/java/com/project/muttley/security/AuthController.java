package com.project.muttley.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.muttley.domain.user.LoginRequest;
import com.project.muttley.services.user.UserAuthService;

@RestController
@RequestMapping("api/auth")
public class AuthController {

  @Autowired
  private AuthenticationManager authManager;

  @Autowired
  private JwtService jwtService;

  @Autowired
  private UserAuthService userAuthService;

  @PostMapping("/login")
  public String login(@RequestBody LoginRequest request) {

    authManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.email(),
            request.password()));

    return jwtService.generateToken(
        userAuthService.loadUserByUsername(request.email()));
  }
}