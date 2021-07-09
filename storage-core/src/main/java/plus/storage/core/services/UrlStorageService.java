package plus.storage.core.services;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpHeaders;
import plus.storage.core.entities.StorageObject;
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
}
