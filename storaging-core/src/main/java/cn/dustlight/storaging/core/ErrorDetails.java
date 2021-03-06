package cn.dustlight.storaging.core;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ErrorDetails {

    private String message;
    private int code;
    private String details;
    @JsonIgnore
    private transient StorageException exception;

    public ErrorDetails(int code, String message) {
        this.code = code;
        this.message = message;
        exception = new StorageException(this);
    }

    public ErrorDetails(int code, String message, Throwable throwable) {
        this.code = code;
        this.message = message;
        exception = new StorageException(this, throwable);
        if (throwable != null)
            this.details = throwable.getMessage();
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public ErrorDetails message(String message) {
        this.message = message;
        return this;
    }

    public ErrorDetails code(int code) {
        this.code = code;
        return this;
    }

    public ErrorDetails details(String details) {
        this.details = details;
        return this;
    }

    public ErrorDetails details(Throwable throwable) {
        if (throwable != null) {
            this.details = throwable.getMessage();
            this.exception.setStackTrace(throwable.getStackTrace());
        }
        return this;
    }

    public void setException(StorageException storageException) {
        this.exception = exception;
    }

    public StorageException getException() {
        return exception;
    }

    public void throwException() throws StorageException {
        throw exception;
    }

    @Override
    public String toString() {
        return "ErrorDetails{" +
                "message='" + message + '\'' +
                ", code=" + code +
                ", details='" + details + '\'' +
                '}';
    }
}
