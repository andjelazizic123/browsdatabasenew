package com.example.browsdatabase.e2e;

import com.example.browsdatabase.BrowsDatabaseApplication;
import com.example.browsdatabase.facade.request.ConnectionRequest;
import com.example.browsdatabase.facade.response.ConnectionResponse;
import com.example.browsdatabase.facade.response.QueryListResponse;
import com.example.browsdatabase.facade.response.QueryResponse;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.LinkedHashMap;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        classes = {BrowsDatabaseApplication.class})
@ExtendWith(SpringExtension.class)
public class ConnectionOperationTest {

    private TestRestTemplate restTemplate = new TestRestTemplate();
    private HttpHeaders customerHeaders = new HttpHeaders();
    private HttpEntity<ConnectionRequest> httpEntity ;
    private ConnectionRequest connectionRequest;

    @Test
    public void should_createConnection() {

        /******  delete connection */
        httpEntity = new HttpEntity<>(customerHeaders);

        // when
        ResponseEntity<ConnectionResponse> responseDelete =  restTemplate.exchange(
                "http://localhost:8081/brows-database/deleteConnection?connectionName=connection_notification",
                HttpMethod.DELETE, httpEntity, ConnectionResponse.class
                 );

        //then
        if (responseDelete.getBody().isSuccessful()) {
            assertThat(responseDelete.getBody()
                    .getSuccessfulMessage()).isEqualTo("Connection 'connection_notification' is successfully deleted");
        }
        else{
             assertThat(responseDelete.getBody().isSuccessful()).isFalse();
             assertThat(responseDelete.getBody()
                    .getErrorMessages().stream().findFirst().get()).isEqualTo("The connection not exist.....");

        }


        /******  create connection */
        // given
        connectionRequest = getConnectionRequest();
        httpEntity = new HttpEntity<>(connectionRequest,customerHeaders);

        // when
        ResponseEntity<ConnectionResponse> responseCreate =  restTemplate.exchange(
                "http://localhost:8081/brows-database/addConnection",
                HttpMethod.POST, httpEntity, ConnectionResponse.class);

        // then
        assertThat(responseCreate.getBody().isSuccessful()).isTrue();

        /****** update connection *********/

        //give
        connectionRequest = getConnectionRequestForUpdate();
        httpEntity = new HttpEntity<>(connectionRequest,customerHeaders);

        // when
        ResponseEntity<ConnectionResponse> responseUpdate =  restTemplate.exchange(
                "http://localhost:8081/brows-database/updateConnection",
                HttpMethod.PUT, httpEntity, ConnectionResponse.class);

        // then
        assertThat(responseCreate.getBody().isSuccessful()).isTrue();

        /****** list all column from table BOOK *********/

        //give
        httpEntity = new HttpEntity<>(customerHeaders);

        // when
        ResponseEntity<QueryListResponse> responseAllColumns =  restTemplate.exchange(
                "http://localhost:8081/brows-database/getColumns?connectionName=connection_notification&tableName=BOOK",
                HttpMethod.GET, httpEntity, QueryListResponse.class);

        assertThat(responseAllColumns.getBody().getResult().size()==5).isTrue();


        /****** getColumn statistic *********/

        // when
        ResponseEntity<QueryResponse> responseColumnStatistic =  restTemplate.exchange(
                "http://localhost:8081/brows-database/getColumnStatistic?connectionName=connection_notification&tableName=BOOK&columnName=price",
                HttpMethod.GET, httpEntity, QueryResponse.class);

        LinkedHashMap linkedHashMap = (LinkedHashMap) responseColumnStatistic.getBody().getResult();

        assertThat(linkedHashMap.get("avgValue").equals(1645.52)).isTrue();
        assertThat(linkedHashMap.get("median").equals(1050.615)).isTrue();

    }


    private  ConnectionRequest  getConnectionRequest(){

        ConnectionRequest connectionRequest = new ConnectionRequest();
        connectionRequest.setName("connection_notification");
        connectionRequest.setHost("localhost");
        connectionRequest.setPort("3307");
        connectionRequest.setDatabase("notification_db");
        connectionRequest.setUser("test");
        connectionRequest.setPassword("testing");

        return connectionRequest;
    }
    private  ConnectionRequest  getConnectionRequestForUpdate(){

        ConnectionRequest connectionRequest = new ConnectionRequest();
        connectionRequest.setName("connection_notification");
        connectionRequest.setHost("localhost");
        connectionRequest.setPort("3307");
        connectionRequest.setDatabase("books_db");
        connectionRequest.setUser("root");
        connectionRequest.setPassword("admin");

        return connectionRequest;
    }
}
