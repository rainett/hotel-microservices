package io.rainett.adminmicroservice.controller;

import io.rainett.adminmicroservice.dto.ValidationErrorDto;
import io.rainett.adminmicroservice.exception.AdminNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorDto> handleValidationException(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        ValidationErrorDto validationErrorDto = new ValidationErrorDto();
        bindingResult.getFieldErrors()
                .forEach(f -> validationErrorDto.addFieldError(f.getField(), f.getDefaultMessage()));
        return ResponseEntity
                .badRequest()
                .body(validationErrorDto);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity
                .internalServerError()
                .body(ex.getMessage());
    }

    @ExceptionHandler(AdminNotFoundException.class)
    public ResponseEntity<String> handleAdminNotFoundException(AdminNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }

}
