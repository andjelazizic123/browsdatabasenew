package com.example.browsdatabase.controllers;

import com.example.browsdatabase.facade.ConnectionFacade;
import com.example.browsdatabase.persistence.ConnectionDB;
import com.example.browsdatabase.facade.request.ConnectionRequest;
import com.example.browsdatabase.facade.response.ConnectionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class ConnectionCreateController {

    private final ConnectionFacade connectionFacade;

    @PostMapping(
            value = "/addConnection",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ConnectionResponse> createConnection(@RequestBody ConnectionRequest request) {
        return ResponseEntity.ok(connectionFacade.crateConnection(request));
     }

    @GetMapping(
            value = "/findAllConnection",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<ConnectionDB>> findAllConnection() {
        List<ConnectionDB> connectionDBList = connectionFacade.findAllConnection();
        return ResponseEntity.status(HttpStatus.OK).body(connectionDBList);
     }

     @PutMapping(
            value = "/updateConnection",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ConnectionResponse> updateConnection(@RequestBody ConnectionRequest request) {
        return ResponseEntity.ok(connectionFacade.updateConnection(request));
    }

    @DeleteMapping(
            value = "/deleteConnection",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ConnectionResponse> deleteConnection(@RequestParam String connectionName) {
        return ResponseEntity.ok(connectionFacade.deleteConnection(connectionName));
    }
}
