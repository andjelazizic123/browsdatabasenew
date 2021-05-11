package com.example.browsdatabase.facade.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConnectionRequest {

    @NotBlank(message = "connection name must not be blank")
    private String name;
    @NotBlank(message = "host must not be blank")
    private String host;
    @NotBlank(message = "database name must not be blank")
    private String database;
    @NotBlank(message = "port must not be blank")
    private String port;
    @NotBlank(message = "user must not be blank")
    private String user;
    @NotBlank(message = "password must not be blank")
    private String password;

    private String type;

}
