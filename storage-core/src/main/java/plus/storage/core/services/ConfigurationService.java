package plus.storage.core.services;

import reactor.core.publisher.Mono;

import java.util.Map;

public interface ConfigurationService {

    Mono<Map<String,?>> getConfiguration(String clientId, String userId, String name);

    Mono<Void> setConfiguration(String clientId,String userId,String name,Map<String,?> config);

}
