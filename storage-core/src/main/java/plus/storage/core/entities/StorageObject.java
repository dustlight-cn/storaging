package plus.storage.core.entities;

import java.util.Map;

public interface StorageObject extends ACL,Datable {

    String getClientId();

    String getId();

    String getName();

    String getDescription();

    String getType();

    Long getSize();

    Map<String,Object> getAdditional();
}
