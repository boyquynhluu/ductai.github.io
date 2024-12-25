package com.qlsv.exceptionhandler;

import java.util.regex.Pattern;

import io.micrometer.common.util.StringUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EmailValidator implements ConstraintValidator<ValidEmail, String> {

    private static final String REGEX_PATTERN = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    private static final Pattern pattern = Pattern.compile(REGEX_PATTERN);

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        return StringUtils.isNotBlank(email) && pattern.matcher(email).matches();
    }
}
