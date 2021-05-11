package com.example.browsdatabase.mapping;

import com.example.browsdatabase.persistence.ConnectionDB;
import com.example.browsdatabase.facade.dto.ConnectionDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel="spring")
public interface MapperConnection {
    ConnectionDB map(ConnectionDTO connectionDTO);
 }

