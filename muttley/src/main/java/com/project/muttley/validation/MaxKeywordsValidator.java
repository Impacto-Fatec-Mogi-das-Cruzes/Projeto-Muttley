package com.project.muttley.validation;

import com.project.muttley.domain.event.EventKeywords;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class MaxKeywordsValidator implements ConstraintValidator<MaxKeywords, String> {

  private int max;

  @Override
  public void initialize(MaxKeywords constraint) {
    max = constraint.max();
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (value == null || value.isBlank()) {
      return true;
    }
    int count = EventKeywords.parse(value).size();
    if (count > max) {
      context.disableDefaultConstraintViolation();
      context.buildConstraintViolationWithTemplate(
          "No máximo " + max + " competências são permitidas").addConstraintViolation();
      return false;
    }
    return true;
  }
}
