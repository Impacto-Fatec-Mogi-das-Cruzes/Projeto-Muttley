package com.project.muttley.exceptions;

public class ParticipantAlreadyExistsException extends RuntimeException {

  public ParticipantAlreadyExistsException(String message) {
    super(message);
  }
}