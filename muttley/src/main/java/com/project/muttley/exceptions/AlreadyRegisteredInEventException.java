package com.project.muttley.exceptions;

public class AlreadyRegisteredInEventException extends RuntimeException {

  public AlreadyRegisteredInEventException(String message) {
    super(message);
  }
}
