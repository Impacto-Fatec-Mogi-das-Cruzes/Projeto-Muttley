package com.project.muttley.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.muttley.services.email.EmailService;

@RestController
@RequestMapping("/api/teste")
public class TestController {

  @Autowired
  private EmailService emailService;

  @GetMapping("/test-email")
  public String test() {
    emailService.sendEmail(
        "joaopsigue7@gmail.com",
        "joaopsigue7@gmail.com",
        "TESTE",
        "FUNCIONOU",
        new byte[0]);
    return "Foi";
  }

}
