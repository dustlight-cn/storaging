package cn.dustlight.storaging.application.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import cn.dustlight.storaging.cloud.services.CloudStorageService;
import cn.dustlight.storaging.core.entities.StorageObject;
import cn.dustlight.storaging.core.services.UrlStorageService;
import cn.dustlight.storaging.mongodb.services.MongoStorageService;

@Configuration
public class StorageServiceConfiguration {

    @Bean
    public UrlStorageService<StorageObject> mongoCloudStorageService(@Autowired MongoStorageService storageService,
                                                                     @Autowired CloudStorageService cloudStorageService) {
        return new UrlStorageService<>(cloudStorageService, storageService);
    }
}
