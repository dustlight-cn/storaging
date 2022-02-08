package cn.dustlight.storaging.mongodb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.util.StringUtils;
import cn.dustlight.storaging.mongodb.services.MongoConfigurationService;
import cn.dustlight.storaging.mongodb.services.MongoStorageService;

@Configuration
@EnableConfigurationProperties(MongoStorageProperties.class)
public class MongoStorageConfiguration {

    @Bean
    public MongoConfigurationService mongoConfigurationService(@Autowired ReactiveMongoOperations operations,
                                                               @Autowired MongoStorageProperties properties) {
        return new MongoConfigurationService(operations,
                StringUtils.hasText(properties.getConfigCollectionName()) ?
                        properties.getConfigCollectionName() :
                        "configs");
    }

    @Bean
    public MongoStorageService mongoStorageService(@Autowired ReactiveMongoOperations operations,
                                                   @Autowired MongoStorageProperties properties){
        return new MongoStorageService(operations,
                StringUtils.hasText(properties.getObjectCollectionName()) ?
                        properties.getObjectCollectionName() :
                        "objects");
    }
}
