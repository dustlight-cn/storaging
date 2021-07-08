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
    public String generateGetUrl(String id, long expiresIn) {
        return urlGenerator.generateGetUrl(id, expiresIn);
    }

    @Override
    public String generatePut(String id, long expiresIn) {
        return urlGenerator.generatePut(id, expiresIn);
    }

    @Override
    public void deleteNow(String id) {
        this.delete(id).block();
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
            urlGenerator.deleteNow(id);
        });
    }
}
