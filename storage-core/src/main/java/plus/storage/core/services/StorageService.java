package plus.storage.core.services;

import plus.storage.core.entities.QueryResult;
import plus.storage.core.entities.StorageObject;
import reactor.core.publisher.Mono;

import java.util.Collection;

public interface StorageService<T extends StorageObject> {

    Mono<T> create(StorageObject object);

    Mono<T> get(String id);

    Mono<Void> put(StorageObject object);

    Mono<Void> delete(String id);

    Mono<Boolean> exists(String id);

    Mono<QueryResult<T>> find(String keywords,
                              int page,
                              int size,
                              String clientId,
                              String owner);
}
