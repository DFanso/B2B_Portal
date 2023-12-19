package com.B2B.Portal.product.configuration;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class StringArrayConverter implements AttributeConverter<String[], String> {

    @Override
    public String convertToDatabaseColumn(String[] attribute) {
        // Join the string array into a single string with a delimiter (e.g., comma)
        return attribute != null ? String.join(",", attribute) : null;
    }

    @Override
    public String[] convertToEntityAttribute(String dbData) {
        // Split the string back into an array
        return dbData != null ? dbData.split(",") : new String[0];
    }
}

