package plus.storage.core.services;

public interface ObjectUrlGenerator {

    String generateGetUrl(String key, long expiresIn);

    String generatePut(String key, long expiresIn);

    void deleteNow(String key);
}
