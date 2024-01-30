package com.cl.clapp.clapprepository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cl.clapp.model.MockData;

public interface Mockdatarepository extends JpaRepository<MockData,UUID>{
    
}
