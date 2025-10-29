package com.example.infinity_webhook_service.exceptions;

public class InvalidEnumValueException extends IllegalArgumentException{
    public InvalidEnumValueException(String msg){
        super(msg);
    }
}
