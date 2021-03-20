package com.digitalinnovation.heroes.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class HeroIdAlreadyExistsException extends Exception {

    public HeroIdAlreadyExistsException(String id) {
        super(String.format("Hero with id %s already registered in the system.",id));
    }
}
