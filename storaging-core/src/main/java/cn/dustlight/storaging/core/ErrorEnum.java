package cn.dustlight.storaging.core;

public enum ErrorEnum {

    NO_ERRORS(-1, "No errors"),
    UNKNOWN(0, "Unknown error"),
    UNAUTHORIZED(1, "Unauthorized"),
    ACCESS_DENIED(2, "Access denied"),

    INPUT_INVALID(1000, "Input invalid"),


    RESOURCE_NOT_FOUND(2000, "Resource not found"),
    OBJECT_NOT_FOUND(2001, "Object not found"),
    CONFIG_NOT_FOUND(2002, "Config not found"),


    RESOURCE_EXISTS(3000, "Resource already exists"),
    OBJECT_EXISTS(3001, "Object already exists"),
    CONFIG_EXISTS(3002, "Config already exists"),

    CREATE_RESOURCE_FAILED(4000, "Fail to create resource"),
    CREATE_OBJECT_FAILED(4001, "Fail to create object"),
    CREATE_CONFIG_FAILED(4002, "Fail to create config"),

    UPDATE_RESOURCE_FAILED(5000, "Fail to update resource"),
    UPDATE_OBJECT_FAILED(5001, "Fail to update object"),
    UPDATE_CONFIG_FAILED(5002, "Fail to update config"),

    DELETE_RESOURCE_FAILED(6000, "Fail to delete resource"),
    DELETE_OBJECT_FAILED(6001, "Fail to delete object"),
    DELETE_CONFIG_FAILED(6002, "Fail to delete config");

    private ErrorDetails details;

    ErrorEnum(int code, String message) {
        this.details = new ErrorDetails(code, message);
    }

    public void throwException() {
        this.details.throwException();
    }

    public ErrorDetails getDetails() {
        return details;
    }

    public int getCode() {
        return this.details.getCode();
    }

    public String getMessage() {
        return this.details.getMessage();
    }

    public String getErrorDetails() {
        return this.details.getDetails();
    }

    public Exception getException(){
        return this.details.getException();
    }

    public ErrorDetails message(String message) {
        return new ErrorDetails(this.details.getCode(), message != null ? message : this.details.getMessage());
    }

    public ErrorDetails details(String details) {
        ErrorDetails instance = new ErrorDetails(this.details.getCode(), this.details.getMessage());
        instance.setDetails(details != null ? details : this.details.getDetails());
        return instance;
    }

    public ErrorDetails details(Throwable throwable) {
        return new ErrorDetails(this.details.getCode(), this.details.getMessage(), throwable);
    }
}
