package com.plantdata.kgcloud.domain.dataset.converter;

import javax.persistence.AttributeConverter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddressConverter implements AttributeConverter<List<String>, String> {
    @Override
    public String convertToDatabaseColumn(List<String> strings) {
        return String.join(",",strings);
    }

    @Override
    public List<String> convertToEntityAttribute(String s) {
        if(s == null){
            return new ArrayList<>();
        }
        return Arrays.asList(s.split(","));
    }
}
