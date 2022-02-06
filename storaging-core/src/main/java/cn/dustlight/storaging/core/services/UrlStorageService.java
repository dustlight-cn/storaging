package cn.dustlight.storaging.core.services;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpHeaders;
import cn.dustlight.storaging.core.entities.QueryResult;
import cn.dustlight.storaging.core.entities.StorageObject;
import reactor.core.publisher.Mono;

@Getter
@Setter
@AllArgsConstructor
public class UrlStorageService<T extends StorageObject> implements ObjectUrlGenerator, StorageService<T> {

    private ObjectUrlGenerator urlGenerator;
    private StorageService storageService;

    @Override
    public Mono<String> generateGetUrl(String id, long expiresIn) {
        return urlGenerator.generateGetUrl(id, expiresIn);
    }

    @Override
    public Mono<String> generatePut(String id, long expiresIn, HttpHeaders httpHeaders) {
        return urlGenerator.generatePut(id, expiresIn, httpHeaders);
    }

    @Override
    public Mono<T> create(StorageObject object) {
        return storageService.create(object);
    }

    @Override
    public Mono<T> get(String id) {
        return storageService.get(id);
    }

    @Override
    public Mono<Void> put(StorageObject object) {
        return storageService.put(object);
    }

    @Override
    public Mono<Void> delete(String id) {
        return storageService.delete(id).doOnSuccess(v -> {
            urlGenerator.delete(id);
        });
    }

    @Override
    public Mono<Boolean> exists(String id) {
        return storageService.exists(id);
    }

    @Override
    public Mono<QueryResult<T>> find(String keywords,
                                     int page,
                                     int size,
                                     String clientId,
                                     String owner) {
        return storageService.find(keywords, page, size, clientId, owner);
    }
}
