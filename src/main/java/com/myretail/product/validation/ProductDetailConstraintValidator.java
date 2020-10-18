package com.myretail.product.validation;

import com.myretail.product.domain.ProductDetailRequest;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 *  This is a validator class to validate the ProductDetailRequest
 *
 *  @author Shrinivaasan Venkataramani
 */
public class ProductDetailConstraintValidator implements ConstraintValidator<ProductDetailConstraint, ProductDetailRequest> {

    @Override
    public void initialize(ProductDetailConstraint constraintAnnotation) {
    }

    /**
     * This method does the validation operation and returns boolean value based of the validation result.
     * @param productDetailRequest which has to validated.
     * @param constraintValidatorContext current context
     * @return boolean value represents validation status.
     */
    @Override
    public boolean isValid(ProductDetailRequest productDetailRequest, ConstraintValidatorContext constraintValidatorContext) {

        return productDetailRequest != null
                && productDetailRequest.getProductPrice() != null
                && !StringUtils.isEmpty(productDetailRequest.getProductPrice().getCurrencyCode())
                && productDetailRequest.getProductPrice().getValue() != null
                && productDetailRequest.getProductPrice().getValue() > 0.0;
    }
}
