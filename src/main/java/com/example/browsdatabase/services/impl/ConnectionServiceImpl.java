package com.example.browsdatabase.services.impl;

import com.example.browsdatabase.error.*;
import com.example.browsdatabase.facade.impl.ConnectionFacadeImpl;
import com.example.browsdatabase.facade.response.ConnectionResponse;
import com.example.browsdatabase.persistence.ConnectionDB;
import com.example.browsdatabase.facade.dto.ConnectionDTO;
import com.example.browsdatabase.mapping.MapperConnection;
import com.example.browsdatabase.repository.ConnectionRepository;
import com.example.browsdatabase.services.ConnectionService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.List;
import java.util.Set;
import java.util.function.Function;


@Service
@RequiredArgsConstructor
public class ConnectionServiceImpl implements ConnectionService {
    private static Logger LOGGER = LoggerFactory.getLogger(ConnectionFacadeImpl.class);
    public static final String CONNECTION_NOT_EXIST = "The connection not exist.....";

    private final MapperConnection mapperConnection;
    private final ConnectionRepository connectionRepository;


    @Override
    public ConnectionResponse createConnection(ConnectionDTO connectionDTO) {
            LOGGER.info("Create new connectionDB {}",connectionDTO.getConnectionName());
            ConnectionDB connectionDB = mapperConnection.map(connectionDTO);
            try{
                 testConnection(connectionDB);
            } catch (SqlConnectionException e) {
                 return new ConnectionResponse(false,null,
                         Set.of(e.getMessage()));
            }
            connectionRepository.save(connectionDB);
            LOGGER.info("New connection correct defined......");
            return new ConnectionResponse(true,
                               "Connection '" + connectionDB.getConnectionName() + "' is successfully created",
                                null);
     }

    @Override
    public List<ConnectionDB> findAllConnection() {
        return connectionRepository.findAll();
    }

    @Override
    public  ConnectionResponse updateConnection(ConnectionDTO connectionDTO){
        List<ConnectionDB> list = connectionRepository.findAllByConnectionName(connectionDTO.getConnectionName());
        if(list.size()==0) throw new ConnectionException(CONNECTION_NOT_EXIST,connectionDTO.getConnectionName());
        ConnectionDB connectionDB = mapperConnection.map(connectionDTO);
        try{
            testConnection(connectionDB);
        } catch (SqlConnectionException e) {
            return new ConnectionResponse(false,null, Set.of(e.getMessage()));
        }
        update(list.get(0),connectionDB);
        connectionRepository.save(list.get(0));
        return new ConnectionResponse(true,
                "Connection '" + connectionDB.getConnectionName() + "' is successfully updated",
                null);
    }

    @Override
    public ConnectionResponse deleteConnection(String connectionName){
        List<ConnectionDB> list = connectionRepository.findAllByConnectionName(connectionName);
        if(list.size()==0) throw new ConnectionException(CONNECTION_NOT_EXIST,connectionName);
        connectionRepository.delete(list.get(0));
        return new ConnectionResponse(true,
                        "Connection '" + connectionName + "' is successfully deleted",
                        null);
    }

    @Override
    public  Connection getConnection(String connectionName){
        List<ConnectionDB> list = connectionRepository.findAllByConnectionName(connectionName);
        if(list.size()==0) throw new ConnectionException(CONNECTION_NOT_EXIST,connectionName);
        try{
            Connection connection =  DriverManager.getConnection(getConnectionURL().apply(list.get(0)),
                    list.get(0).getUserName(),
                    list.get(0).getPassword());
            LOGGER.info("the connection \"" + connectionName+ "\" is established ......");
            return connection;
        }catch (SQLException e) {
            throw new SqlConnectionException(e.getMessage());
        }
    }

    private boolean testConnection(ConnectionDB connectionDB){
        try(Connection conn = DriverManager.getConnection(getConnectionURL().apply(connectionDB),
                connectionDB.getUserName(),
                connectionDB.getPassword())) {
            return true;
        }
        catch (SQLException e) {
            throw new SqlConnectionException(e.getMessage());
        }
    }

    private Function<ConnectionDB,String> getConnectionURL(){
        return connectionDb -> "jdbc:mysql://"
                + connectionDb.getHostName() + ":"
                + connectionDb.getPort() +"/"
                + connectionDb.getDatabaseName()
                + "?serverTimezone=UTC&characterEncoding=UTF-8&useSSL=false&allowPublicKeyRetrieval=true";
    }

    private void update(ConnectionDB oldConnection, ConnectionDB newConnection){
        oldConnection.setUserName(newConnection.getUserName());
        oldConnection.setPassword(newConnection.getPassword());
        oldConnection.setPort(newConnection.getPort());
        oldConnection.setDatabaseName(newConnection.getDatabaseName());
        oldConnection.setHostName(newConnection.getHostName());
    }

 }