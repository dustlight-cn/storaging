package cn.dustlight.storaging.cloud.services;

import cn.dustlight.storage.core.Permission;
import cn.dustlight.storage.core.RestfulStorage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpHeaders;
import cn.dustlight.storaging.core.StorageException;
import cn.dustlight.storaging.core.services.ObjectUrlGenerator;
import reactor.core.publisher.Mono;

import java.io.IOException;

@AllArgsConstructor
@Getter
public class CloudStorageService implements ObjectUrlGenerator {

    private RestfulStorage storage;

    @Override
    public Mono<String> generateGetUrl(String key, long expiresIn) {
        try {
            return Mono.just(storage.generateGetUrl(key, expiresIn));
        } catch (IOException e) {
            return Mono.error(new StorageException("Fail to generate get url: " + key, e));
        }
    }

    @Override
    public Mono<String> generatePut(String key, long expiresIn, HttpHeaders httpHeaders) {
        try {
            if (httpHeaders == null)
                return Mono.just(storage.generatePutUrl(key, Permission.PRIVATE, expiresIn));
            return Mono.just(storage.generatePutUrl(key, Permission.PRIVATE, expiresIn, httpHeaders.toSingleValueMap()));
        } catch (IOException e) {
            return Mono.error(new StorageException("Fail to generate put url: " + key, e));
        }
    }

    @Override
    public Mono<Void> deleteObject(String key) {
        try {
            storage.remove(key);
            return Mono.empty();
        } catch (IOException e) {
            return Mono.error(new StorageException("Fail to delete object now: " + key, e));
        }
    }
}
