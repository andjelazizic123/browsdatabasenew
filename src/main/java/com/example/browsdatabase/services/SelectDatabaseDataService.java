package com.example.browsdatabase.services;

import com.example.browsdatabase.common.ColumnDB;
import com.example.browsdatabase.common.ColumnStatisticData;
import com.example.browsdatabase.common.TableStatisticData;
import com.example.browsdatabase.facade.response.QueryListResponse;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

public interface SelectDatabaseDataService {
    QueryListResponse selectAllTables(Connection connection, String connectionName);
    QueryListResponse selectAllColumnsForTable(Connection connection, String connectionName, String tableName);
    Map<ColumnDB, List<String>> selectAllDataForTable(Connection connection, String connectionName, String tableName);
    ColumnStatisticData getColumnStatistic(Connection connection, String connectionName, String  tableName, String columnName);
    TableStatisticData getTableStatistic(Connection connection, String connectionName, String tableName);
}
