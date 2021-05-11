package com.example.browsdatabase.facade;


import com.example.browsdatabase.facade.response.ConnectionResponse;
import com.example.browsdatabase.facade.response.QueryListResponse;
import com.example.browsdatabase.facade.response.QueryResponse;
import com.example.browsdatabase.persistence.ConnectionDB;
import com.example.browsdatabase.facade.request.ConnectionRequest;


import java.util.List;


public interface ConnectionFacade {
    ConnectionResponse crateConnection(ConnectionRequest connectionDTO);
    List<ConnectionDB> findAllConnection();
    ConnectionResponse updateConnection(ConnectionRequest connectionRequest);
    ConnectionResponse deleteConnection(String connectionName);
    QueryListResponse selectAllTables(String connectionName);
    QueryListResponse selectAllColumnsForTable(String connectionName, String tableName);
    QueryResponse selectAllDataForTable(String connectionName, String tableName);
    QueryResponse getColumnStatistic(String connectionName, String tableName, String columnName);
    QueryResponse getTableStatistic(String connectionName, String tableName);

}
