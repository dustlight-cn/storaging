package plus.storage.mongodb.services;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.StringUtils;
import plus.storage.core.ErrorEnum;
import plus.storage.core.StorageException;
import plus.storage.core.entities.BaseStorageObject;
import plus.storage.core.entities.QueryResult;
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
        return operations.insert(origin, collectionName)
                .onErrorMap(throwable -> ErrorEnum.CREATE_OBJECT_FAILED.details(throwable.getMessage()).getException());
    }

    @Override
    public Mono<BaseStorageObject> get(String id) {
        return operations.findById(id, BaseStorageObject.class, collectionName)
                .onErrorMap(throwable -> new StorageException("Get object failed: " + throwable.getMessage(), throwable))
                .switchIfEmpty(Mono.error(ErrorEnum.OBJECT_NOT_FOUND.getException()));
    }

    @Override
    public Mono<Void> put(StorageObject object) {
        Update update = new Update();
        if (object.getName() != null)
            update.set("name", object.getName());
        if (object.getDescription() != null)
            update.set("description", object.getDescription());
        if (object.getOwner() != null)
            update.set("owner", object.getOwner());
        if (object.getCanRead() != null)
            update.set("canRead", object.getCanRead());
        if (object.getCanWrite() != null)
            update.set("canRead", object.getCanWrite());
        if (object.getSize() != null)
            update.set("size", object.getSize());
        if (object.getType() != null)
            update.set("type", object.getType());
        if (object.getClientId() != null)
            update.set("clientId", object.getClientId());

        update.set("updatedAt", new Date());
        return operations.updateFirst(Query.query(where("_id").is(object.getId())),
                update,
                BaseStorageObject.class,
                collectionName)
                .onErrorMap(throwable -> ErrorEnum.UPDATE_OBJECT_FAILED.details(throwable.getMessage()).getException())
                .doOnSuccess(updateResult -> {
                    if (updateResult.getMatchedCount() == 0)
                        ErrorEnum.OBJECT_NOT_FOUND.throwException();
                })
                .then();
    }

    @Override
    public Mono<Void> delete(String id) {
        return operations.findAndRemove(new Query(where("_id").is(id)), BaseStorageObject.class, collectionName)
                .onErrorMap(throwable -> ErrorEnum.DELETE_OBJECT_FAILED.details(throwable.getMessage()).getException())
                .switchIfEmpty(Mono.error(ErrorEnum.OBJECT_NOT_FOUND.getException()))
                .then();
    }

    @Override
    public Mono<Boolean> exists(String id) {
        return operations.exists(new Query(where("_id").is(id)), BaseStorageObject.class, collectionName);
    }

    @Override
    public Mono<QueryResult<BaseStorageObject>> find(String keywords,
                                                     int page,
                                                     int size,
                                                     String clientId,
                                                     String owner) {
        return StringUtils.hasText(keywords) ?
                operations.count(Query.query(TextCriteria.forDefaultLanguage().matching(keywords))
                                .addCriteria(where("clientId").is(clientId)
                                        .and("owner").elemMatch(new Criteria("$eq").is(owner))),
                        BaseStorageObject.class,
                        collectionName)
                        .flatMap(c -> operations.find(Query.query(TextCriteria.forDefaultLanguage().matching(keywords))
                                        .addCriteria(where("clientId").is(clientId)
                                                .and("owner").elemMatch(new Criteria("$eq").is(owner)))
                                        .with(Pageable.ofSize(size).withPage(page)),
                                BaseStorageObject.class,
                                collectionName)
                                .collectList()
                                .map(baseStorageObjects -> new QueryResult<>(c, baseStorageObjects))
                        ) :
                operations.count(Query.query(where("clientId").is(clientId)
                                .and("owner").elemMatch(new Criteria("$eq").is(owner))),
                        BaseStorageObject.class,
                        collectionName)
                        .flatMap(c -> operations.find(Query.query(where("clientId").is(clientId)
                                        .and("owner").elemMatch(new Criteria("$eq").is(owner)))
                                        .with(Pageable.ofSize(size).withPage(page)),
                                BaseStorageObject.class,
                                collectionName)
                                .collectList()
                                .map(baseStorageObjects -> new QueryResult<>(c, baseStorageObjects))
                        );
    }


}
