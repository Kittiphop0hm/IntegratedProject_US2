package com.example.backend.validation;

import com.example.backend.dtos.users.RegisterFormDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.StringUtils;

public class SellerFieldsValidator implements ConstraintValidator<ValidSellerFields, RegisterFormDto> {

    @Override
    public boolean isValid(RegisterFormDto dto, ConstraintValidatorContext context) {
        if (dto == null) return true; // let other checks handle null body

        // ถ้าไม่ใช่ seller ให้ผ่าน
        if (!"seller".equalsIgnoreCase(dto.getUserType())) {
            return true;
        }

        boolean valid = true;
        context.disableDefaultConstraintViolation(); // เราจะสร้าง violation ระบุ field

        if (!StringUtils.hasText(dto.getPhoneNumber())) {
            context.buildConstraintViolationWithTemplate("phoneNumber must not be blank for seller")
                    .addPropertyNode("phoneNumber")
                    .addConstraintViolation();
            valid = false;
        }

        if (!StringUtils.hasText(dto.getBankAccount())) {
            context.buildConstraintViolationWithTemplate("bankAccount must not be blank for seller")
                    .addPropertyNode("bankAccount")
                    .addConstraintViolation();
            valid = false;
        }

        if (!StringUtils.hasText(dto.getCardNumber())) {
            context.buildConstraintViolationWithTemplate("cardNumber must not be blank for seller")
                    .addPropertyNode("cardNumber")
                    .addConstraintViolation();
            valid = false;
        }

        return valid;
    }
}
