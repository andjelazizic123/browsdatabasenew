package com.example.browsdatabase.error;

import lombok.Data;

@Data
public class ConnectionException extends RuntimeException{
    private String message;
    private String connectionName;
    public ConnectionException(String message, String connectionName) {
        this.message = message;
        this.connectionName = connectionName;
    }
}
