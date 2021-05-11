package com.example.browsdatabase.controllers;

import com.example.browsdatabase.facade.ConnectionFacade;
import com.example.browsdatabase.facade.response.QueryListResponse;
import com.example.browsdatabase.facade.response.QueryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SelectDatabaseDataController {

    private final ConnectionFacade connectionFacade;

    @GetMapping(
            value = "/getTables",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<QueryListResponse> getTables(@RequestParam String connectionName) {
        return ResponseEntity.ok(connectionFacade.selectAllTables(connectionName));
    }

    @GetMapping(
            value = "/getColumns",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<QueryListResponse> getColumns(@RequestParam String connectionName,
                                                    @RequestParam String tableName) {
        return ResponseEntity.ok(connectionFacade.selectAllColumnsForTable(connectionName,tableName));
    }

    @GetMapping(
            value = "/getTableData",
                produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<QueryResponse> getTableData(@RequestParam String connectionName,
                                                          @RequestParam String tableName) {
        return ResponseEntity.ok(connectionFacade.selectAllDataForTable(connectionName,tableName));
    }

    @GetMapping(
            value = "/getColumnStatistic",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<QueryResponse> getColumnStatistic(@RequestParam String connectionName,
                                                                       @RequestParam String tableName,
                                                                       @RequestParam String columnName) {
        return ResponseEntity.ok(connectionFacade.getColumnStatistic(connectionName,tableName,columnName));
    }

    @GetMapping(
            value = "/getTableStatistic",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<QueryResponse> getColumnStatistic(@RequestParam String connectionName,
                                                                 @RequestParam String tableName) {
        return ResponseEntity.ok(connectionFacade.getTableStatistic(connectionName,tableName));
    }

}
