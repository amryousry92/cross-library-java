package com.crossover.techtrial.exception;

public class TransactionException extends Exception {

    int status;

    public int getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }

    String message;

    public TransactionException(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
