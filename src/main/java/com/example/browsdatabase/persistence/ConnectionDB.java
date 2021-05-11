package com.example.browsdatabase.persistence;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@Table(name = "CONNECTION")
public class ConnectionDB {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    private String id;

    @Column(name = "name")
    private String connectionName;

    @Column(name = "host_name")
    private String hostName;

    @Column(name = "port")
    private String port;

    @Column(name = "database_name")
    private String databaseName;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "password")
    private String password;

    @Column(name = "database_type")
    private String databaseType;

}
