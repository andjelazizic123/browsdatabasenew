package com.example.browsdatabase.facade.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueryResponse {
    private boolean isSuccessful;
    private String connectionName;
    private Object result;
    private Set<String> errorMessages;

}
