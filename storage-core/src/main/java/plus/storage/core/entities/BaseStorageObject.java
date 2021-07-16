package plus.storage.core.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

@Getter
@Setter
public class BaseStorageObject implements StorageObject {

    private Collection<String> owner,canRead,canWrite;

    private Date updatedAt,createdAt;

    private String clientId,id,name,description,type;

    private Long size;

    private Map<String,Object> additional;

}
