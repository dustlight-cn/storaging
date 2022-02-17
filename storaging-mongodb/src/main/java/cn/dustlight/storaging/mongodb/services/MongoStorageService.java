package cn.dustlight.storaging.mongodb.services;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.StringUtils;
import cn.dustlight.storaging.core.ErrorEnum;
import cn.dustlight.storaging.core.entities.BaseStorageObject;
import cn.dustlight.storaging.core.entities.QueryResult;
import cn.dustlight.storaging.core.entities.StorageObject;
import cn.dustlight.storaging.core.services.StorageService;
import reactor.core.publisher.Mono;

import java.util.Date;

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
                .onErrorMap(throwable -> ErrorEnum.CREATE_OBJECT_FAILED.details(throwable).getException());
    }

    @Override
    public Mono<BaseStorageObject> get(String id, String uid, String clientId) {
        Criteria criteria = Criteria.where("_id").is(id).and("clientId").is(clientId);
        if (StringUtils.hasText(uid))
            criteria.orOperator(Criteria.where("owner").in(uid),
                    Criteria.where("canRead").in(uid));
        Query query = Query.query(criteria);
        return operations.findOne(query, BaseStorageObject.class, collectionName)
                .onErrorMap(throwable -> ErrorEnum.OBJECT_NOT_FOUND.details(throwable).getException())
                .switchIfEmpty(Mono.error(ErrorEnum.OBJECT_NOT_FOUND.getException()));
    }

    @Override
    public Mono<Void> put(StorageObject object, String uid, String clientId) {
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

        update.set("updatedAt", new Date());

        Criteria criteria = Criteria.where("_id").is(object.getId()).and("clientId").is(clientId);
        if (StringUtils.hasText(uid))
            criteria.orOperator(Criteria.where("owner").in(uid));
        Query query = Query.query(criteria);
        return operations.updateFirst(query,
                        update,
                        BaseStorageObject.class,
                        collectionName)
                .onErrorMap(throwable -> ErrorEnum.UPDATE_OBJECT_FAILED.details(throwable).getException())
                .flatMap(updateResult -> updateResult.getMatchedCount() == 0 ?
                        Mono.error(ErrorEnum.OBJECT_NOT_FOUND.getException())
                        : Mono.empty());
    }

    @Override
    public Mono<Void> delete(String id, String uid, String clientId) {
        Criteria criteria = Criteria.where("_id").is(id).and("clientId").is(clientId);
        if (StringUtils.hasText(uid))
            criteria.and("owner").in(uid);
        Query query = Query.query(criteria);
        return operations.findAndRemove(query, BaseStorageObject.class, collectionName)
                .onErrorMap(throwable -> ErrorEnum.DELETE_OBJECT_FAILED.details(throwable).getException())
                .switchIfEmpty(Mono.error(ErrorEnum.OBJECT_NOT_FOUND.getException()))
                .then();
    }

    @Override
    public Mono<Boolean> exists(String id, String uid, String clientId) {
        Criteria criteria = Criteria.where("_id").is(id).and("clientId").is(clientId);
        if (StringUtils.hasText(uid))
            criteria.orOperator(Criteria.where("owner").in(uid),
                    Criteria.where("canRead").in(uid));
        Query query = Query.query(criteria);
        return operations.exists(query, BaseStorageObject.class, collectionName);
    }

    @Override
    public Mono<QueryResult<BaseStorageObject>> find(String keywords,
                                                     int page,
                                                     int size,
                                                     String clientId,
                                                     String uid) {
        Criteria criteria = Criteria.where("clientId").is(clientId);
        if (StringUtils.hasText(uid))
            criteria.orOperator(Criteria.where("owner").in(uid),
                    Criteria.where("canRead").in(uid));
        Query query = Query.query(criteria);

        if (StringUtils.hasText(keywords))
            query.addCriteria(TextCriteria.forDefaultLanguage().matching(keywords));
        return operations.count(query, collectionName)
                .flatMap(count -> operations.find(query
                                        .with(Pageable.ofSize(size).withPage(page))
                                        .with(Sort.by(Sort.Order.desc("createdAt"))),
                                BaseStorageObject.class,
                                collectionName)
                        .collectList()
                        .map(objects -> new QueryResult<>(count, objects))
                );
    }


}
