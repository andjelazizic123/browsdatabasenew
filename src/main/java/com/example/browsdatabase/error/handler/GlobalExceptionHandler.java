package com.example.browsdatabase.error.handler;

import com.example.browsdatabase.error.ConnectionAttributeValueException;
import com.example.browsdatabase.error.ConnectionException;
import com.example.browsdatabase.facade.response.ConnectionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import java.util.Set;


@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    private static Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ConnectionAttributeValueException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ConnectionResponse handleException(ConnectionAttributeValueException connectionAttributeValueException) {
        LOGGER.error("Error in attribute values {}",connectionAttributeValueException);
        return new ConnectionResponse(false,null,connectionAttributeValueException.getViolations());
    }

    @ExceptionHandler(ConnectionException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ConnectionResponse handleException(ConnectionException connectionException) {
        LOGGER.error("Error !!!!!" +  connectionException.getMessage() + " {}", connectionException);
        return new ConnectionResponse(false,null,Set.of(connectionException.getMessage()));
    }
}
