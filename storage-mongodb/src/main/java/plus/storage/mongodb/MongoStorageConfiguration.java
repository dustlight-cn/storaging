package plus.storage.mongodb;

import com.mongodb.reactivestreams.client.MongoClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.util.StringUtils;
import plus.storage.mongodb.services.MongoConfigurationService;

@Configuration
@EnableConfigurationProperties(MongoStorageProperties.class)
public class MongoStorageConfiguration {

    @Bean
    @ConditionalOnBean(ReactiveMongoDatabaseFactory.class)
    public MongoConfigurationService mongoConfigurationService(@Autowired ReactiveMongoDatabaseFactory factory,
                                                               @Autowired MongoStorageProperties properties) {
        ReactiveMongoTemplate mongoTemplate = null;
        if (StringUtils.hasText(properties.getDatabase())) {
            mongoTemplate = new ReactiveMongoTemplate(MongoClients.create(), properties.getDatabase());
        } else {
            mongoTemplate = new ReactiveMongoTemplate(factory);
        }
        return new MongoConfigurationService(mongoTemplate,
                StringUtils.hasText(properties.getConfigCollectionName()) ?
                        properties.getConfigCollectionName() :
                        "config");
    }
}
