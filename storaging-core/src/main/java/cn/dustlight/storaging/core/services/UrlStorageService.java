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
    public Mono<String> generateGetUrl(String key, long expiresIn) {
        return urlGenerator.generateGetUrl(key, expiresIn);
    }

    @Override
    public Mono<String> generatePut(String key, long expiresIn, HttpHeaders httpHeaders) {
        return urlGenerator.generatePut(key, expiresIn, httpHeaders);
    }

    @Override
    public Mono<Void> deleteObject(String key) {
        return urlGenerator.deleteObject(key);
    }

    @Override
    public Mono<T> create(StorageObject object) {
        return storageService.create(object);
    }

    @Override
    public Mono<T> get(String id, String uid, String clientId) {
        return storageService.get(id, uid, clientId);
    }

    @Override
    public Mono<Void> put(StorageObject object, String uid, String clientId) {
        return storageService.put(object, uid, clientId);
    }

    @Override
    public Mono<Void> delete(String id, String uid, String clientId) {
        return storageService.delete(id, uid, clientId);
    }

    @Override
    public Mono<Boolean> exists(String id, String uid, String clientId) {
        return storageService.exists(id, uid, clientId);
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
