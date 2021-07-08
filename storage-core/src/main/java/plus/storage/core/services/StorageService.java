package plus.storage.core.services;

import plus.storage.core.entities.StorageObject;
import reactor.core.publisher.Mono;

public interface StorageService<T extends StorageObject> {

    Mono<T> create(T object);

    Mono<T> get(String key);

    Mono<Void> put(T object);

    Mono<Void> delete(String key);

}
