package com.example.browsdatabase.facade.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueryListResponse {
    private boolean isSuccessful;
    private String connectionName;
    private List<?> result;
    private Set<String> errorMessages;
}
