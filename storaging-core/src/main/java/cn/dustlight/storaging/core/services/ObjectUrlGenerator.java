package cn.dustlight.storaging.core.services;


import org.springframework.http.HttpHeaders;
import reactor.core.publisher.Mono;

public interface ObjectUrlGenerator {

    Mono<String> generateGetUrl(String key, long expiresIn);

    Mono<String> generatePut(String key, long expiresIn, HttpHeaders httpHeaders);

    Mono<Void> deleteObject(String key);
}
