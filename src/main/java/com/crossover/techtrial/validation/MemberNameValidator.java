package com.crossover.techtrial.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MemberNameValidator implements
    ConstraintValidator<MemberNameConstraint, String> {

    @Override
    public void initialize(MemberNameConstraint memberName) {
    }

    @Override
    public boolean isValid(String memberName,
        ConstraintValidatorContext cxt) {
        return memberName != null && memberName.charAt(0) < 'z' && memberName.charAt(0)> 'A'
            && (memberName.length() > 1) && (memberName.length() < 101);
    }
}
