package plus.storage.cloud;

import cn.dustlight.storage.core.RestfulStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import plus.storage.cloud.services.CloudStorageService;

@Configuration
public class StorageCloudConfiguration {

    @Bean
    @ConditionalOnBean(RestfulStorage.class)
    public CloudStorageService alibabaCloudStorageService(@Autowired RestfulStorage storage){
        return new CloudStorageService(storage);
    }
}
