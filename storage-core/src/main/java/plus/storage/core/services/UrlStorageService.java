package plus.storage.core.services;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import plus.storage.core.entities.StorageObject;
import reactor.core.publisher.Mono;

@Getter
@Setter
@AllArgsConstructor
public class UrlStorageService<T extends StorageObject> implements ObjectUrlGenerator, StorageService<T> {

    private ObjectUrlGenerator urlGenerator;
    private StorageService storageService;

    @Override
    public String generateGetUrl(String key, long expiresIn) {
        return urlGenerator.generateGetUrl(key, expiresIn);
    }

    @Override
    public String generatePut(String key, long expiresIn) {
        return urlGenerator.generatePut(key, expiresIn);
    }

    @Override
    public void deleteNow(String key) {
        this.delete(key).block();
    }

    @Override
    public Mono<T> create(T object) {
        return storageService.create(object);
    }

    @Override
    public Mono<T> get(String key) {
        return storageService.get(key);
    }

    @Override
    public Mono<Void> put(T object) {
        return storageService.put(object);
    }

    @Override
    public Mono<Void> delete(String key) {
        return storageService.delete(key).doOnSuccess(v -> {
            urlGenerator.deleteNow(key);
        });
    }
}
