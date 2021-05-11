package com.example.browsdatabase.services.impl;

import com.example.browsdatabase.common.ColumnDB;
import com.example.browsdatabase.common.ColumnStatisticData;
import com.example.browsdatabase.common.TableStatisticData;
import com.example.browsdatabase.error.SqlConnectionException;
import com.example.browsdatabase.error.ConnectionException;
import com.example.browsdatabase.facade.response.QueryListResponse;
import com.example.browsdatabase.services.SelectDatabaseDataService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.*;
import java.sql.Date;
import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

@Service
public class SelectDatabaseDataServiceImpl implements SelectDatabaseDataService {

    private static final String PK = "1";
    public static final String COLUMN_NOT_EXISTS_OR_NOT_NUMBER = "The columns not exists or column is not of the number data type ....... ";

    @Override
    public QueryListResponse selectAllTables(Connection connection, String connectionName){
        List<String> tables  = new ArrayList<>();
        try{
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            ResultSet resultSetTables = databaseMetaData.getTables(null, null, "%", new String[]{"TABLE"});
            while(resultSetTables.next()){
                tables.add(resultSetTables.getString("TABLE_NAME"));
            }
            return new QueryListResponse(true,connectionName,tables,null);
        }
        catch (SQLException e){
            throw new SqlConnectionException(e.getMessage());
        }
    }

    @Override
    public QueryListResponse selectAllColumnsForTable(Connection connection,
                                                      String connectionName,
                                                      String tableName) {
        List<ColumnDB> columnDBS = new ArrayList<>();
        try{
            DatabaseMetaData meta = connection.getMetaData();
            ResultSet resultSet = meta.getColumns(null, null, tableName, null);
            ResultSet resultSetPK = meta.getPrimaryKeys( connection.getCatalog(), null, tableName);

            while(resultSet.next()){
                ColumnDB columnDB = new ColumnDB();
                columnDB.setTableName(tableName);
                columnDB.setColumnName(resultSet.getString("COLUMN_NAME"));
                columnDB.setColumnOrder(resultSet.getInt("ORDINAL_POSITION"));
                columnDB.setColumnDataType(resultSet.getString("TYPE_NAME"));
                columnDB.setColumnSize(resultSet.getInt("COLUMN_SIZE"));
                columnDB.setNullable(resultSet.getString("IS_NULLABLE"));
                if(resultSetPK.next() && resultSetPK.getString("KEY_SEQ").equals(PK)) {
                    columnDB.setPrimaryKey("YES");
                }
                columnDBS.add(columnDB);
            }
            return new QueryListResponse(true, connectionName, columnDBS,null);
        }
        catch (SQLException e){
            throw new SqlConnectionException(e.getMessage());
        }
    }

  @Override
  public Map<ColumnDB,List<String>> selectAllDataForTable(Connection connection, String connectionName, String tableName) {
      Map<ColumnDB,List<String>> mapColumnsValues = new TreeMap<>();
      QueryListResponse queryListResponse = selectAllColumnsForTable(connection,connectionName,tableName);
      try{
          if(queryListResponse.getResult().size() > 0) mapColumnsValues = loadDataByColumn(connection, queryListResponse);
          return mapColumnsValues;
      }
      catch (SQLException e){
          throw new SqlConnectionException(e.getMessage());
      }
    }


    @Override
    public ColumnStatisticData getColumnStatistic(Connection connection, String connectionName, String tableName, String columnName) {
        ColumnDB columnDB = isColumnNumber(connection, connectionName, tableName, columnName);
        ColumnStatisticData columnStatisticData = new ColumnStatisticData();
        columnStatisticData.setColumnName(columnName);
        columnStatisticData.setTableName(tableName);
        calculateColumnStatistic(connection,columnDB,columnStatisticData,connectionName);
        return columnStatisticData;
    }

    @Override
    public TableStatisticData getTableStatistic(Connection connection, String connectionName, String tableName){
        TableStatisticData tableStatisticData = new TableStatisticData();
        tableStatisticData.setTableName(tableName);
        try {
            calculateBasicTableStatistic(connection, tableStatisticData,connectionName);
        } catch (SQLException e) {
            throw  new SqlConnectionException(e.getMessage());
        }
        calculateColumnNumbersOfTable(connection, tableStatisticData,connectionName);
        return tableStatisticData;
    }


    private Map<ColumnDB,List<String>> loadDataByColumn(Connection connection,
                                                        QueryListResponse queryListResponseAllColumns) throws SQLException{

        List<ColumnDB> listOfColumnDBMetaData = queryListResponseAllColumns.getResult().stream().map(element->(ColumnDB)element).sorted().collect(Collectors.toList());
        Map<ColumnDB,List<String>> mapColumnsData = initializeResultLikeMap(listOfColumnDBMetaData);
        String query = "select * from " + listOfColumnDBMetaData.get(0).getTableName() + " order by id";

        try(var statement = connection.prepareStatement(query);
                ResultSet resultSet =  statement.executeQuery()) {
            while (resultSet.next()) {
                loadRowSetInMap(mapColumnsData,resultSet);
            }
        }
        return mapColumnsData;
    }

    private Map<ColumnDB,List<String>> initializeResultLikeMap(List<ColumnDB> listOfColumnDBMetaData){
        Map<ColumnDB,List<String>> mapColumnsValues = new TreeMap<>();
        listOfColumnDBMetaData.stream().forEach(column -> mapColumnsValues.put(column,new ArrayList<>()));
        return mapColumnsValues;
    }

    private void  loadRowSetInMap(Map<ColumnDB,List<String>> mapColumnsData, ResultSet resultSet) throws SQLException {
        for (Map.Entry<ColumnDB, List<String>> entry : mapColumnsData.entrySet()) {
             addColumnDataInColumnMapEntity(entry, resultSet);
        }
    }

    private void  addColumnDataInColumnMapEntity(Map.Entry<ColumnDB,List<String>> entry, ResultSet rs) throws SQLException{
        Object valueObject = rs.getObject(entry.getKey().getColumnName());
        if(valueObject instanceof String) entry.getValue().add((String)valueObject);
        if(valueObject instanceof Date) entry.getValue().add(((Date)valueObject).toString());
        if(valueObject instanceof BigDecimal) entry.getValue().add(((BigDecimal)valueObject).toString());
        if(valueObject instanceof Long) entry.getValue().add(((Long)valueObject).toString());
        if(valueObject instanceof Integer) entry.getValue().add(((Integer)valueObject).toString());
        if(valueObject instanceof Double) entry.getValue().add(((Double)valueObject).toString());
        if(valueObject instanceof Boolean) entry.getValue().add(((Boolean)valueObject).toString());
    }

    private Optional<ColumnDB> getColumnMetaData(Connection connection, String tableName, String columnName) {
        QueryListResponse queryListResponse = selectAllColumnsForTable(connection,tableName,columnName);

        return  queryListResponse.getResult()
                .stream()
                .map(element->(ColumnDB)element)
                .filter(column -> column.getColumnName().contentEquals(columnName))
                .findFirst();
    }

    private ColumnStatisticData calculateColumnStatistic(Connection connection, ColumnDB columnDB,
                                                   ColumnStatisticData columnStatisticData,String connectionName){

        String queryBasic = getBasicStatisticQuery().apply(columnDB.getColumnName(), columnDB.getTableName());
        String queryMedian =  getQueryForMedian().apply(columnDB.getColumnName(), columnDB.getTableName());
        calculateColumnBasicStatistic(connection,queryBasic,columnStatisticData,connectionName);
        calculateMedian(connection,queryMedian,columnStatisticData,connectionName);
        return columnStatisticData;

    }

    private void calculateBasicTableStatistic(Connection connection, TableStatisticData tableStatisticData, String connectionName ) throws SQLException{
        String query =  getQueryForTableStatistic().apply(connection.getCatalog().toString(), tableStatisticData.getTableName());
        try(var statement = connection.prepareStatement(query);
            ResultSet resultSet =  statement.executeQuery()){
            if(resultSet.next()) {
                tableStatisticData.setNumberOfColumn(resultSet.getInt("Rows"));
                tableStatisticData.setAverageRowLength(resultSet.getDouble("Avg_row_length"));
                tableStatisticData.setDataLength(resultSet.getInt("Data_length"));
              }
        }
    }

    private void calculateColumnNumbersOfTable(Connection connection, TableStatisticData tableStatisticData, String connectionName ){
        QueryListResponse queryListResponse = selectAllColumnsForTable( connection, connectionName, tableStatisticData.getTableName());
        tableStatisticData.setNumberOfColumn(queryListResponse.getResult().size());
    }

    private BiFunction<String,String,String> getBasicStatisticQuery(){
        return (columnName,tableName) -> "Select max(" + columnName + "), min(" + columnName+ "),avg(" + columnName+") " +
                "from " + tableName;
    }

    private BiFunction<String,String,String> getQueryForMedian(){
        return (columnName,tableName) ->  "SELECT AVG(a.val) as median_val " +
        "FROM ( SELECT " + columnName  + " as 'val', @rownum:=@rownum+1 as `row_number`, @total_rows:=@rownum " +
        "FROM " + tableName + ", (SELECT @rownum:=0) r " +
        "WHERE " + columnName + "  is NOT NULL " +
        "ORDER BY " + columnName+ ") as a " +
        "WHERE a.row_number IN ( FLOOR((@total_rows+1)/2), FLOOR((@total_rows+2)/2) )";
    }



    private BiFunction<String,String,String> getQueryForTableStatistic(){
        return (dataBaseName,tableName) -> "SHOW TABLE STATUS FROM " + dataBaseName + " LIKE '" + tableName +"'";

    }

    private void calculateColumnBasicStatistic(Connection connection, String query, ColumnStatisticData columnStatisticData, String connectionName){
         try(var statement = connection.prepareStatement(query);
            ResultSet resultSet =  statement.executeQuery()) {
            if(resultSet.next()) {
                columnStatisticData.setMaxValue(resultSet.getBigDecimal(1));
                columnStatisticData.setMinValue(resultSet.getBigDecimal(2));
                columnStatisticData.setAvgValue(resultSet.getBigDecimal(3));
            }
        }catch (SQLException e) {
             throw  new SqlConnectionException(e.getMessage());
         }
     }

    private void calculateMedian(Connection connection, String query, ColumnStatisticData columnStatisticData,String connectionName) {
        try(var statement = connection.prepareStatement(query);
            ResultSet resultSet =  statement.executeQuery()) {
            if(resultSet.next()) columnStatisticData.setMedian(resultSet.getBigDecimal(1));
        }
        catch (SQLException e) {
            throw  new SqlConnectionException(e.getMessage());
        }
    }

    private ColumnDB isColumnNumber(Connection connection, String connectionName, String tableName, String columnName){
        QueryListResponse queryListResponse = selectAllColumnsForTable(connection,connectionName,tableName);
        Optional<ColumnDB> optionColumnDB = queryListResponse.getResult().stream()
                .map(element-> (ColumnDB)element)
                .filter(column->column.getColumnDataType().equals("DOUBLE")||
                                column.getColumnDataType().equals("INTEGER")||
                                column.getColumnDataType().equals("SMALLINT")||
                                column.getColumnDataType().equals("NUMERIC"))
                .filter(column->column.getColumnName().equals(columnName)).findFirst();
        optionColumnDB.orElseThrow(()-> new ConnectionException(COLUMN_NOT_EXISTS_OR_NOT_NUMBER, connectionName));
        return optionColumnDB.get();
    }
 }



