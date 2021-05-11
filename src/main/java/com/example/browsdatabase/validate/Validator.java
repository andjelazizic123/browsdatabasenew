package com.example.browsdatabase.validate;

import com.example.browsdatabase.facade.request.ConnectionRequest;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Validator {

    private final ConnectionAttributeValidator connectionAttributeValidator;
    private final ConnectionRuleValidator connectionRuleValidator;

    public void validateCreateConnection(ConnectionRequest connectionRequest) {
        connectionAttributeValidator.validate(connectionRequest);
        connectionRuleValidator.validate(connectionRequest);
    }

    public void validateUpdateConnection(ConnectionRequest connectionRequest) {
        connectionAttributeValidator.validate(connectionRequest);
    }



}
