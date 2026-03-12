package com.sasagui.onmangequoi.dish;

import java.text.MessageFormat;

public class DishAlreadyExistsException extends RuntimeException {
    public DishAlreadyExistsException(String dishLabel) {
        super(MessageFormat.format("A dish with label ''{0}'' already exists", dishLabel));
    }
}
