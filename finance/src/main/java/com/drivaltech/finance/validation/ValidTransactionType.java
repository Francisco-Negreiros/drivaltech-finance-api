package com.drivaltech.finance.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Constraint(validatedBy = TransactionTypeValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidTransactionType {

    String message() default "Invalid transaction type";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
