package ru.practicum.shareit.exceptions;

public class UnknownStateError {
    private int statusCode;
    private String message;

    public UnknownStateError() {
    }

    public UnknownStateError(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
