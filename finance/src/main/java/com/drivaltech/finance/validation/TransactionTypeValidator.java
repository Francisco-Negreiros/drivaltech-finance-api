package com.drivaltech.finance.validation;

import com.drivaltech.finance.domain.TransactionType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TransactionTypeValidator
        implements ConstraintValidator<ValidTransactionType, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (value == null || value.isBlank()) {
            return true; // deixa @NotBlank tratar isso
        }

        try {
            TransactionType.valueOf(value.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}