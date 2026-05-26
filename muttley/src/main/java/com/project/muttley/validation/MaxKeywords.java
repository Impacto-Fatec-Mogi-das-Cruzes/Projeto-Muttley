package com.project.muttley.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = MaxKeywordsValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface MaxKeywords {

  String message() default "No máximo {max} competências são permitidas";

  int max() default 10;

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
