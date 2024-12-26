package com.microservice.order.validation;

import jakarta.validation.Payload;

public @interface ValidEnum {

    Class<? extends Enum<?>> value();
    String message() default "Invalid value for enum";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};


}
