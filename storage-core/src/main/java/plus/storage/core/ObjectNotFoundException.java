package plus.storage.core;

public class ObjectNotFoundException extends StorageException {

    public ObjectNotFoundException(String message) {
        super(message);
    }

    public ObjectNotFoundException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
