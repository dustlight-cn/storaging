package plus.storage.core;

public class ObjectAccessDeniedException extends StorageException {

    public ObjectAccessDeniedException(String message) {
        super(message);
    }

    public ObjectAccessDeniedException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
