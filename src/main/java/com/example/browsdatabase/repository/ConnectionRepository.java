package com.example.browsdatabase.repository;

import com.example.browsdatabase.persistence.ConnectionDB;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConnectionRepository extends JpaRepository<ConnectionDB, String> {
    List<ConnectionDB> findAllByConnectionName(String name);

}
