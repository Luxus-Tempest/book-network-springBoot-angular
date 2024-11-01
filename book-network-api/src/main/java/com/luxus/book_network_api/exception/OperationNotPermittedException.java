package com.luxus.book_network_api.exception;

public class OperationNotPermittedException extends RuntimeException {


    public OperationNotPermittedException(String message) {
        super(message);
    }
    //Aller dans globalExceptionHandler pour ajouter la gestion de cette exception
}
