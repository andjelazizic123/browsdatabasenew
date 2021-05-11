package com.example.browsdatabase.facade.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConnectionResponse {
    @JsonProperty
    private boolean isSuccessful;
    @JsonProperty
    private String successfulMessage;
    @JsonProperty
    private Set<String> errorMessages;
}
