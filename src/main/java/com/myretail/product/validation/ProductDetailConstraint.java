package com.myretail.product.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.Documented;

/**
 * This is a constraint used to validate ProductDetailRequest bean.
 *
 * @author Shrinivaasan Venkataramani
 */
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ProductDetailConstraintValidator.class)
@Documented
public @interface ProductDetailConstraint {
    String message() default "Invalid product detail";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
