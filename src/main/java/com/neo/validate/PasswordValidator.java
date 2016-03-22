package com.neo.validate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by neowyp on 2016/3/22.
 */
public class PasswordValidator implements ConstraintValidator<Password, String> {
    //5~10位的数字与字母组合
    private static Pattern pattern = Pattern.compile("(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{5,10}");

    public void initialize(Password constraintAnnotation) {
        //do nothing
    }

    public boolean isValid(String value, ConstraintValidatorContext context) {
        if( value==null ){
            return false;
        }
        Matcher m = pattern.matcher(value);
        return m.matches();
    }
}
