package plus.storage.mongodb.services;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import plus.storage.core.ObjectNotFoundException;
import plus.storage.core.entities.BaseStorageObject;
import plus.storage.core.entities.StorageObject;
import plus.storage.core.services.StorageService;
import reactor.core.publisher.Mono;

import java.util.Date;

import static org.springframework.data.mongodb.core.query.Criteria.*;

@Getter
@Setter
@AllArgsConstructor
public class MongoStorageService implements StorageService<BaseStorageObject> {

    private ReactiveMongoOperations operations;
    private String collectionName;

    @Override
    public Mono<BaseStorageObject> create(StorageObject object) {
        BaseStorageObject origin = new BaseStorageObject();

        origin.setOwner(object.getOwner());
        origin.setCanRead(object.getCanRead());
        origin.setCanWrite(object.getCanWrite());

        origin.setClientId(object.getClientId());
        origin.setDescription(object.getDescription());
        origin.setName(object.getName());
        origin.setSize(object.getSize());
        origin.setType(object.getType());
        origin.setId(null);

        Date t = new Date();
        origin.setCreatedAt(t);
        origin.setUpdatedAt(t);
        return operations.insert(origin,collectionName);
    }

    @Override
    public Mono<BaseStorageObject> get(String key) {
        return operations.findById(key,BaseStorageObject.class,collectionName);
    }

    @Override
    public Mono<Void> put(StorageObject object) {
        Update update = new Update();
        if(object.getName() != null)
            update.set("name",object.getName());
        if(object.getDescription() != null)
            update.set("description",object.getDescription());
        if(object.getOwner() != null)
            update.set("owner",object.getOwner());
        if(object.getCanRead() != null)
            update.set("canRead",object.getCanRead());
        if(object.getCanWrite() != null)
            update.set("canRead",object.getCanWrite());
        if(object.getSize() != null)
            update.set("size",object.getSize());
        if(object.getType() != null)
            update.set("type",object.getType());
        if(object.getClientId() != null)
            update.set("clientId",object.getClientId());

        update.set("updatedAt", new Date());
        return operations.updateFirst(Query.query(where("_id").is(object.getId())),
                update,
                BaseStorageObject.class,
                collectionName)
                .then();
    }

    @Override
    public Mono<Void> delete(String key) {
        return operations.findAndRemove(new Query(where("_id").is(key)),BaseStorageObject.class,collectionName)
                .flatMap(object -> {
                    if(object == null)
                        return Mono.error(new ObjectNotFoundException("Object not found: " + key));
                    return Mono.empty();
                });
    }

}
