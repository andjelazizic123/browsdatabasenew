package com.example.browsdatabase.mapping;

import com.example.browsdatabase.facade.dto.ConnectionDTO;
import com.example.browsdatabase.facade.request.ConnectionRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel="spring")
public interface MapperConnectionRequest {

    @Mappings({
            @Mapping(target = "connectionName", source = "name"),
            @Mapping(target = "hostName", source = "host"),
            @Mapping(target = "databaseName", source = "database"),
            @Mapping(target = "userName", source = "user"),
            @Mapping(target = "databaseType", source = "type")
    })
    ConnectionDTO map(ConnectionRequest connectionRequest);
}
