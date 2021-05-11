package com.example.browsdatabase.mapping;

import com.example.browsdatabase.facade.dto.ConnectionDTO;
import com.example.browsdatabase.facade.request.ConnectionRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel="spring")
public interface MapperNewConnection {
    ConnectionDTO map(ConnectionRequest connectionRequest);
}
