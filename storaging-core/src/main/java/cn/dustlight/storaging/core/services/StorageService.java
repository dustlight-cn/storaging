package cn.dustlight.storaging.core.services;

import cn.dustlight.storaging.core.entities.QueryResult;
import cn.dustlight.storaging.core.entities.StorageObject;
import reactor.core.publisher.Mono;

public interface StorageService<T extends StorageObject> {

    Mono<T> create(StorageObject object);

    Mono<T> get(String id,String uid,String clientId);

    Mono<Void> put(StorageObject object,String uid,String clientId);

    Mono<Void> delete(String id,String uid,String clientId);

    Mono<Boolean> exists(String id,String uid,String clientId);

    Mono<QueryResult<T>> find(String keywords,
                              int page,
                              int size,
                              String clientId,
                              String uid);
}
