package plus.storage.core.services;

public interface ObjectUrlGenerator {

    String generateGetUrl(String id, long expiresIn);

    String generatePut(String id, long expiresIn);

    void deleteNow(String id);
}
