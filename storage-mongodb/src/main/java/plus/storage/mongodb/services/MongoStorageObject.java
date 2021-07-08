package plus.storage.mongodb.services;

import org.springframework.data.mongodb.core.mapping.MongoId;
import plus.storage.core.entities.BaseStorageObject;

public class MongoStorageObject extends BaseStorageObject {

    @Override
    @MongoId
    public String getKey() {
        return super.getKey();
    }
}
