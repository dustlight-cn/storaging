package plus.storage.cloud;

import cn.dustlight.storage.alibaba.oss.AlibabaCloudObjectStorage;
import cn.dustlight.storage.tencent.cos.TencentCloudObjectStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import plus.storage.cloud.services.CloudStorageService;

@Configuration
public class StorageCloudConfiguration {

    @Bean
    @ConditionalOnBean(TencentCloudObjectStorage.class)
    public CloudStorageService tencentCloudStorageService(@Autowired TencentCloudObjectStorage storage){
        return new CloudStorageService(storage);
    }

    @Bean
    @ConditionalOnBean(AlibabaCloudObjectStorage.class)
    public CloudStorageService alibabaCloudStorageService(@Autowired AlibabaCloudObjectStorage storage){
        return new CloudStorageService(storage);
    }
}
