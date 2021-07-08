package plus.storage.mongodb;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "plus.storage")
public class MongoStorageProperties {

    private String database,configCollectionName,fileCollectionName;

}
