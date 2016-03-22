package com.neo.test.validate;

import com.neo.validate.PayTraceDO;
import com.neo.validate.ValidationResult;
import com.neo.validate.ValidationUtils;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

/**
 * Created by neowyp on 2016/3/22.
 */
@Slf4j
public class TestHibernateValidate {

    public static void main(String[] args) {
        try {
            PayTraceDO payTraceDO = new PayTraceDO();
            payTraceDO.setAmount(0);
            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<PayTraceDO>> constraintViolations = validator.validate(payTraceDO);
            log.info("payTraceDO is {}", payTraceDO);
            for (ConstraintViolation<PayTraceDO> constraintViolation : constraintViolations) {
                log.info("validator res {}", constraintViolation);
            }

            ValidationResult result = ValidationUtils.validateEntity(payTraceDO);
            log.info("result is {}", result);
        } catch (Exception e) {
            log.error("", e);
        }
    }
}
