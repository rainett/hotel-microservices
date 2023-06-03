package io.rainett.managermicroservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ManagerNotFoundException extends RuntimeException {

    public ManagerNotFoundException(Long id) {
        super("Manager with id = [" + id + "] was not found");
    }

}
