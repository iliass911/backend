// src/main/java/com/example/backend/exception/ResourceNotFoundException.java
package com.sebn.brettbau.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}

