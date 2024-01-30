package com.cl.clapp.clapprepository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cl.clapp.model.Property;

public interface PropertiesRepository extends JpaRepository<Property,UUID>{

/**
 * @return
 */
@Query(value = "SELECT CAST (property_value -> 'New Year'AS VARCHAR) from property where property_name = 'holidays'",nativeQuery = true)
List<String> getHolidayDatesListFromProperties();

@Query(value = "SELECT * from property where property_name = 'holidays'",nativeQuery = true)
Property getHolidaysListFromProperties();
    
}
