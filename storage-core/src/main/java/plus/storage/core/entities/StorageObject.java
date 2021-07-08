package plus.storage.core.entities;

public interface StorageObject extends ACL,Datable {

    String getClientId();

    String getKey();

    String getName();

    String getDescription();

    String getType();
}
