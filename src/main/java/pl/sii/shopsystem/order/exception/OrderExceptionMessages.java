package pl.sii.shopsystem.order.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum OrderExceptionMessages {
    UUID_HAS_INVALID_FORM("Passed UUID has invalid form: "),
    PRODUCT_NOT_FOUND("Product with given ID was not found: "),
    PRODUCT_QUANTITY_IS_NOT_NUMBER("Product quantity is not number: "),
    PRODUCT_QUANTITY_LOWER_OR_EQUAL_ZERO("Product quantity is not a positive number: ");

    private final String message;

}