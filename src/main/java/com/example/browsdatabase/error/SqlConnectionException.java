package com.example.browsdatabase.error;
import lombok.Data;

@Data
public class SqlConnectionException extends RuntimeException{
    private String message;
    public SqlConnectionException(String message ) {
        this.message = message;
    }
}
