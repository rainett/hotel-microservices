package io.rainett.adminmicroservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AdminNotFoundException extends RuntimeException {
    public AdminNotFoundException(Long id) {
        super("Admin with id = [" + id + "] was not found");
    }
}
