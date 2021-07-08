package plus.storage.cloud.services;

import cn.dustlight.storage.core.Permission;
import cn.dustlight.storage.core.RestfulStorage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import plus.storage.core.services.ObjectUrlGenerator;

import java.io.IOException;

@AllArgsConstructor
@Getter
public class CloudStorageService implements ObjectUrlGenerator {

    private RestfulStorage storage;

    @Override
    public String generateGetUrl(String key, long expiresIn) {
        try {
            return storage.generateGetUrl(key, expiresIn);
        } catch (IOException e) {
            throw new RuntimeException("Fail to generate get url: " + key, e);
        }
    }

    @Override
    public String generatePut(String key, long expiresIn) {
        try {
            return storage.generatePutUrl(key, Permission.PRIVATE, expiresIn);
        } catch (IOException e) {
            throw new RuntimeException("Fail to generate put url: " + key, e);
        }
    }

    @Override
    public void deleteNow(String key) {
        try {
            storage.remove(key);
        } catch (IOException e) {
            throw new RuntimeException("Fail to delete object now: " + key, e);
        }
    }
}
