package com.example.browsdatabase.validate;

import com.example.browsdatabase.error.ConnectionException;
import com.example.browsdatabase.persistence.ConnectionDB;
import com.example.browsdatabase.repository.ConnectionRepository;
import com.example.browsdatabase.facade.request.ConnectionRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ConnectionRuleValidator extends RuntimeException{

    private static final String CONNECTION_NAME_NOT_UNIQUE = "The connection name must be unique";
    private final ConnectionRepository connectionRepository;

    public void validate(ConnectionRequest connectionRequest) {
        checkIsConnectionNameUnique(connectionRequest);
    }
    private void checkIsConnectionNameUnique(ConnectionRequest connectionRequest){
        List<ConnectionDB> connectionDBList = connectionRepository.findAllByConnectionName(connectionRequest.getName());
        if(connectionDBList.size()>0) {
            throw new ConnectionException(CONNECTION_NAME_NOT_UNIQUE,connectionDBList.get(0).getConnectionName());
        }
    }
}
