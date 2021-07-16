package plus.storage.core;

public class StorageException extends RuntimeException {

    private ErrorDetails errorDetails;

    public StorageException(String message) {
        super(message);
        this.errorDetails = new ErrorDetails(0,message);
    }

    public StorageException(String message, Throwable throwable) {
        super(message, throwable);
        this.errorDetails = new ErrorDetails(0,message);
    }

    public StorageException(ErrorDetails errorDetails) {
        super(errorDetails.getMessage());
        this.errorDetails = errorDetails;
    }

    public StorageException(ErrorDetails errorDetails, Throwable throwable) {
        super(errorDetails.getMessage(), throwable);
        this.errorDetails = errorDetails;
    }

    public ErrorDetails getErrorDetails() {
        return errorDetails;
    }

    public void setErrorDetails(ErrorDetails errorDetails) {
        this.errorDetails = errorDetails;
    }
}
