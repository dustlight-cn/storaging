package cn.dustlight.storaging.mongodb.services;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import static org.springframework.data.mongodb.core.query.Criteria.where;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import cn.dustlight.storaging.core.entities.Configuration;
import cn.dustlight.storaging.core.services.ConfigurationService;
import reactor.core.publisher.Mono;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class MongoConfigurationService implements ConfigurationService {

    private ReactiveMongoOperations operations;
    private String collectionName;

    protected static Log logger = LogFactory.getLog(MongoConfigurationService.class);

    @Override
    public Mono<Map<String, ?>> getConfiguration(String clientId, String userId, String name) {
        Query q = new Query(where("clientId").is(clientId).and("userId").is(userId).and("name").is(name));
        return operations.findOne(q, Configuration.class,collectionName)
                .map(configuration -> {
                    if(configuration == null)
                        return null;
                    else
                        return configuration.getData();
                });
    }

    @Override
    public Mono<Void> setConfiguration(String clientId, String userId, String name, Map<String, ?> config) {
        Query q = new Query(where("clientId").is(clientId).and("userId").is(userId).and("name").is(name));
        return operations.upsert(q,new Update().set("data",config),Configuration.class,collectionName)
                .then();
    }
}
