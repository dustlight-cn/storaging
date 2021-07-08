package plus.storage.mongodb;

import com.mongodb.reactivestreams.client.MongoClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.util.StringUtils;
import plus.storage.mongodb.services.MongoConfigurationService;
import plus.storage.mongodb.services.MongoStorageService;

@Configuration
@EnableConfigurationProperties(MongoStorageProperties.class)
public class MongoStorageConfiguration {

    @Bean
    @ConditionalOnBean(ReactiveMongoDatabaseFactory.class)
    public MongoConfigurationService mongoConfigurationService(@Autowired ReactiveMongoOperations operations,
                                                               @Autowired MongoStorageProperties properties) {
        return new MongoConfigurationService(operations,
                StringUtils.hasText(properties.getConfigCollectionName()) ?
                        properties.getConfigCollectionName() :
                        "configs");
    }

    @Bean
    @ConditionalOnBean(ReactiveMongoDatabaseFactory.class)
    public MongoStorageService mongoStorageService(@Autowired ReactiveMongoOperations operations,
                                                   @Autowired MongoStorageProperties properties){
        return new MongoStorageService(operations,
                StringUtils.hasText(properties.getObjectCollectionName()) ?
                        properties.getObjectCollectionName() :
                        "objects");
    }
}
