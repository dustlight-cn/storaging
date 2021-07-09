package plus.storage.core.services;


import org.springframework.http.HttpHeaders;
import reactor.core.publisher.Mono;

public interface ObjectUrlGenerator {

    Mono<String> generateGetUrl(String id, long expiresIn);

    Mono<String> generatePut(String id, long expiresIn, HttpHeaders httpHeaders);

    Mono<Void> delete(String id);
}
