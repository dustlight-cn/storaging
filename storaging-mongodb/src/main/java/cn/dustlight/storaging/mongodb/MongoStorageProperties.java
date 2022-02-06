package cn.dustlight.storaging.mongodb;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "dustlight.storaging")
public class MongoStorageProperties {

    private String configCollectionName = "configs",objectCollectionName = "objects";

}
