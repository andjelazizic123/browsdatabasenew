package com.example.browsdatabase.validate;

import com.example.browsdatabase.error.ConnectionAttributeValueException;
import com.example.browsdatabase.facade.request.ConnectionRequest;
import org.springframework.stereotype.Component;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import java.util.Set;

@Component
public class ConnectionAttributeValidator {
    public void validate(ConnectionRequest connectionRequest) {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        javax.validation.Validator validator = validatorFactory.usingContext().getValidator();
        Set<ConstraintViolation<ConnectionRequest>> violations = validator.validate(connectionRequest);
        if (!violations.isEmpty()) {
            throw new ConnectionAttributeValueException(violations);
        }
    }
}
