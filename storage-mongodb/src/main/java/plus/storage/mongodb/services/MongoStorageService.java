package plus.storage.mongodb.services;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import plus.storage.core.services.StorageService;
import reactor.core.publisher.Mono;

import java.util.Date;

import static org.springframework.data.mongodb.core.query.Criteria.*;

@Getter
@Setter
@AllArgsConstructor
public class MongoStorageService implements StorageService<MongoStorageObject> {

    private ReactiveMongoOperations operations;
    private String collectionName;

    @Override
    public Mono<MongoStorageObject> create(MongoStorageObject object) {
        Date t = new Date();
        object.setCreatedAt(t);
        object.setUpdatedAt(t);
        return operations.save(object,collectionName);
    }

    @Override
    public Mono<MongoStorageObject> get(String key) {
        return operations.findOne(new Query(where("key").is(key)),MongoStorageObject.class,collectionName);
    }

    @Override
    public Mono<Void> put(MongoStorageObject object) {
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
        return operations.updateFirst(new Query(where("key").is(object.getKey())),
                update,
                MongoStorageObject.class,
                collectionName)
                .then();
    }

    @Override
    public Mono<Void> delete(String key) {
        return operations
                .remove(new Query(where("key").is(key)), collectionName)
                .then();
    }

}
