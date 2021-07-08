package plus.storage.core.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.Date;

@Getter
@Setter
public class BaseStorageObject implements StorageObject {

    private Collection<String> owner,canRead,canWrite;

    private Date updatedAt,createdAt;

    private String clientId,key,name,description,type;

    private Long size;

}
