package com.bojan.lora.exception;

public class LoraException extends RuntimeException {
    public LoraException(Exception e) {
        super(e);
    }

    public LoraException(String message) {
        super(new Exception(message));
    }
}
