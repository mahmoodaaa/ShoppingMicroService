package com.microservice.order.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class EnumValidator implements ConstraintValidator<ValidEnum, Enum<?>> {
private Class<? extends Enum<?>> enumClass;
@Override
public void initialize(ValidEnum constraintAnnotation) {
    this.enumClass = constraintAnnotation.value();
}

@Override
public boolean isValid(Enum<?> value, ConstraintValidatorContext context) {
    if (value == null) {
        return false; // Adjust based on whether null is allowed
    }
    return Arrays.stream(enumClass.getEnumConstants())
            .anyMatch(e -> e.name().equals(value.name()));
}

}


