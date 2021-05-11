package com.example.browsdatabase.services;

import com.example.browsdatabase.facade.dto.ConnectionDTO;
import com.example.browsdatabase.facade.response.ConnectionResponse;

import com.example.browsdatabase.persistence.ConnectionDB;
import java.sql.Connection;
import java.util.List;


public interface ConnectionService {

    ConnectionResponse createConnection(ConnectionDTO connectionDTO);
    List<ConnectionDB> findAllConnection();
    ConnectionResponse updateConnection(ConnectionDTO connectionDTO);
    ConnectionResponse deleteConnection(String connectionName);
    Connection getConnection(String connectionName);

}
