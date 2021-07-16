package plus.storage.application.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import plus.storage.cloud.services.CloudStorageService;
import plus.storage.core.entities.StorageObject;
import plus.storage.core.services.UrlStorageService;
import plus.storage.mongodb.services.MongoStorageService;

@Configuration
public class StorageServiceConfiguration {

    @Bean
    public UrlStorageService<StorageObject> mongoCloudStorageService(@Autowired MongoStorageService storageService,
                                                                     @Autowired CloudStorageService cloudStorageService) {
        return new UrlStorageService<>(cloudStorageService, storageService);
    }
}
