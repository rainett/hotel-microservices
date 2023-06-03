package io.rainett.managermicroservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
public class ValidationErrorDto {

    private final Map<String, String> fieldErrors = new HashMap<>();

    public void addFieldError(String fieldName, String errorMessage) {
        fieldErrors.put(fieldName, errorMessage);
    }

}
