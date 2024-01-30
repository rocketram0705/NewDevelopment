package com.cl.clapp.dto;

import java.util.LinkedHashMap;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import org.postgresql.util.PGobject;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Converter(autoApply = true)
public class JsonConverter implements AttributeConverter<LinkedHashMap<String,String>,Object>  {
    
    private static final long serialVersionUID = 1L;
    private static final ObjectMapper mapper = new ObjectMapper();
    //mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES,true);
    @Override
    public Object convertToDatabaseColumn(LinkedHashMap<String,String> arg0) {
        // TODO Auto-generated method stub
        try {
            PGobject out = new PGobject();
            out.setType("json");
            out.setValue(arg0.toString());
            return out;
        } catch (Exception e) {
            // TODO: handle exception
            throw new IllegalArgumentException("Unable to Serialize Json Object",e);
        }
    }

    @Override
    public LinkedHashMap<String,String> convertToEntityAttribute(Object dataValue) {
        try {
            
            if(dataValue instanceof PGobject && ((PGobject) dataValue).getType().equals("json")){
                mapper.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
                System.out.println("<<<<>>>>>"+dataValue.toString()+"<<<<>>>>>");
                System.out.println("writevalueasString direct "+mapper.writeValueAsString(dataValue));
                System.out.println("writepgObjectString"+mapper.writeValueAsString(((PGobject) dataValue).getValue()));
               if(dataValue.toString().contains(":")){
                return mapper.readerFor(new TypeReference<LinkedHashMap<String,String>>() {}).readValue(((PGobject) dataValue).getValue());
               }                 
               
            }
        } catch (Exception e) {
            // TODO: handle exception
            throw new IllegalArgumentException("Unable to Deserialize Json Object",e);
        }
        return null;
    }
    
}
