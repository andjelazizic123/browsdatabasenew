package com.example.browsdatabase.facade.impl;

import com.example.browsdatabase.common.ColumnDB;
import com.example.browsdatabase.common.ColumnStatisticData;
import com.example.browsdatabase.common.TableStatisticData;
import com.example.browsdatabase.error.SqlConnectionException;
import com.example.browsdatabase.facade.dto.ConnectionDTO;
import com.example.browsdatabase.facade.response.ConnectionResponse;
import com.example.browsdatabase.facade.response.QueryListResponse;
import com.example.browsdatabase.facade.response.QueryResponse;
import com.example.browsdatabase.mapping.MapperConnectionRequest;
import com.example.browsdatabase.persistence.ConnectionDB;
import com.example.browsdatabase.facade.request.ConnectionRequest;
import com.example.browsdatabase.facade.ConnectionFacade;
import com.example.browsdatabase.services.ConnectionService;
import com.example.browsdatabase.services.SelectDatabaseDataService;
import com.example.browsdatabase.validate.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.Set;


@Slf4j
@Component
@RequiredArgsConstructor
public class ConnectionFacadeImpl implements ConnectionFacade {

    private static Logger LOGGER = LoggerFactory.getLogger(ConnectionFacadeImpl.class);

    private final ConnectionService connectionService;
    private final SelectDatabaseDataService selectDatabaseDataService;

    private final Validator validator;
    private final MapperConnectionRequest mapperNewConnectionRequest;

    @Override
    public ConnectionResponse crateConnection(ConnectionRequest connectionRequest) {
        ConnectionDTO connectionDTO = prepareCreateConnectionDTO(connectionRequest);
        return connectionService.createConnection(connectionDTO);
     }

    @Override
    public List<ConnectionDB> findAllConnection() {
        return connectionService.findAllConnection();
    }

    @Override
    public ConnectionResponse updateConnection(ConnectionRequest connectionRequest) {
            ConnectionDTO connectionDTO = prepareUpdateConnectionDTO(connectionRequest);
            return connectionService.updateConnection(connectionDTO);
     }

   @Override
    public ConnectionResponse deleteConnection(String connectionName) {
        return connectionService.deleteConnection(connectionName);
    }

    @Override
    public QueryListResponse selectAllTables(String connectionName){
        try{
            Connection connection = connectionService.getConnection(connectionName);
            return selectDatabaseDataService.selectAllTables(connection,connectionName);
        }
        catch (SqlConnectionException e){
            return new QueryListResponse(false,connectionName,null, Set.of(e.getMessage()));
        }
    }

    @Override
    public QueryListResponse selectAllColumnsForTable(String connectionName, String tableName) {
        try{
            Connection connection = connectionService.getConnection(connectionName);
            return selectDatabaseDataService.selectAllColumnsForTable(connection,connectionName, tableName);
        }
        catch (SqlConnectionException e){
            return new QueryListResponse(false,connectionName,null, Set.of(e.getMessage()));
        }
    }

    @Override
    public QueryResponse selectAllDataForTable(String connectionName, String tableName) {
        try{
            Connection connection = connectionService.getConnection(connectionName);
            Map<ColumnDB, List<String>> result = selectDatabaseDataService.selectAllDataForTable(connection,connectionName, tableName);
            return new QueryResponse(true,connectionName,result,null);
        }
        catch (SqlConnectionException e){
            return new QueryResponse(false,connectionName,null, Set.of(e.getMessage()));
        }
    }

    @Override
    public QueryResponse getColumnStatistic(String connectionName, String tableName, String columnName) {
        try{
            Connection connection = connectionService.getConnection(connectionName);
            ColumnStatisticData result =  selectDatabaseDataService.getColumnStatistic(connection,connectionName, tableName,columnName);
            return new QueryResponse(true,connectionName,result,null);
        } catch (SqlConnectionException e){
            return new QueryResponse(false,connectionName,null, Set.of(e.getMessage()));
        }
    }

    @Override
    public QueryResponse getTableStatistic(String connectionName, String tableName) {
        try{
            Connection connection = connectionService.getConnection(connectionName);
            TableStatisticData result =  selectDatabaseDataService.getTableStatistic(connection,connectionName, tableName);
            return new QueryResponse(true,connectionName,result,null);
        } catch (SqlConnectionException e){
            return new QueryResponse(false,connectionName,null, Set.of(e.getMessage()));
        }
    }

    private ConnectionDTO prepareCreateConnectionDTO(ConnectionRequest connectionRequest){
        validator.validateCreateConnection(connectionRequest);
        return mapperNewConnectionRequest.map(connectionRequest);
    }

    private ConnectionDTO prepareUpdateConnectionDTO(ConnectionRequest connectionRequest){
        validator.validateUpdateConnection(connectionRequest);
        return mapperNewConnectionRequest.map(connectionRequest);
    }

}
