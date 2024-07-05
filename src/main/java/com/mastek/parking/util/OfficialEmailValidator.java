package com.mastek.parking.util;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class OfficialEmailValidator implements ConstraintValidator<OfficialEmail, String> {

    private static final String OFFICIAL_EMAIL_REGEX = "^[\\w-\\.]+@mastek\\.com$";
    @Override
    public void initialize(OfficialEmail constraintAnnotation) {
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null) {
            return false;
        }
        return Pattern.compile(OFFICIAL_EMAIL_REGEX).matcher(email).matches();
    }

}
