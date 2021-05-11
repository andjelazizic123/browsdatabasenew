package com.example.browsdatabase.facade.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConnectionDTO {
    private String connectionName;
    private String hostName;
    private String databaseName;

    private String port;
    private String userName;
    private String password;
    private String databaseType;
}
