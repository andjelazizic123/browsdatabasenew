package com.example.browsdatabase.error;

import com.example.browsdatabase.facade.request.ConnectionRequest;
import lombok.Data;
import javax.validation.ConstraintViolation;
import java.util.Set;
import java.util.stream.Collectors;


@Data
public class ConnectionAttributeValueException extends RuntimeException{

    private Set<String> violations;
    public ConnectionAttributeValueException(Set<ConstraintViolation<ConnectionRequest>> violations){
        this.violations = violations.stream().map(violation->violation.getMessage()).collect(Collectors.toSet());
    }
}
