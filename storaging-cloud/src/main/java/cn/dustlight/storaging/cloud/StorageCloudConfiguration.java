package cn.dustlight.storaging.cloud;

import cn.dustlight.storage.core.RestfulStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import cn.dustlight.storaging.cloud.services.CloudStorageService;

@Configuration
public class StorageCloudConfiguration {

    @Bean
    @ConditionalOnBean(RestfulStorage.class)
    public CloudStorageService alibabaCloudStorageService(@Autowired RestfulStorage storage){
        return new CloudStorageService(storage);
    }
}
