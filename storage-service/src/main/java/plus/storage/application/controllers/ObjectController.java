package plus.storage.application.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.server.resource.authentication.AbstractOAuth2TokenAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import plus.auth.resources.AuthPrincipalUtil;
import plus.auth.resources.core.AuthPrincipal;
import plus.storage.core.entities.BaseStorageObject;
import plus.storage.core.entities.StorageObject;
import plus.storage.core.services.UrlStorageService;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.logging.Logger;

@RestController
@RequestMapping("/v1/objects")
@SecurityRequirement(name = "auth")
@Tag(name = "Objects", description = "对象存储服务，提供 Key-Value 的对象存储。")
public class ObjectController {

    @Autowired
    private UrlStorageService storageService;


    @Operation(summary = "创建对象", description = "创建对象")
    @PostMapping("")
    public Mono<StorageObject> createObject(ServerWebExchange exchange,
                                            AbstractOAuth2TokenAuthenticationToken principal) {

        AuthPrincipal authPrincipal = AuthPrincipalUtil.getAuthPrincipal(principal);

        BaseStorageObject origin = new BaseStorageObject();
        origin.setClientId(authPrincipal.getClientId());
        if (origin.getOwner() == null) {
            origin.setOwner(Arrays.asList(authPrincipal.getUid().toString()));
        } else {
            origin.getOwner().add(authPrincipal.getUid().toString());
        }
        return storageService.create(origin);
    }

    @GetMapping("/{id}")
    public Mono<StorageObject> getObject(@PathVariable(name = "id") String id,
                                         AbstractOAuth2TokenAuthenticationToken principal) {
        return storageService.get(id);
    }

    @PutMapping("/{id}")
    public Mono<Void> putObject(@PathVariable(name = "id") String id,
                                @RequestBody BaseStorageObject object,
                                AbstractOAuth2TokenAuthenticationToken principal) {
        object.setId(id);
        return storageService.put(object);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteObject(@PathVariable(name = "id") String id,
                                   AbstractOAuth2TokenAuthenticationToken principal) {
        return storageService.delete(id);
    }

    @GetMapping("/{id}/data")
    public Mono<Void> getObjectData(@PathVariable(name = "id") String id,
                                    ServerWebExchange exchange,
                                    AbstractOAuth2TokenAuthenticationToken principal) {
        return Mono.just(storageService.generateGetUrl(id, 1000 * 60 * 5))
                .flatMap(s -> {

                    Logger.getGlobal().info(s);
                    exchange.getResponse().setStatusCode(HttpStatus.TEMPORARY_REDIRECT);
                    exchange.getResponse().getHeaders().add("Location", s);
                    return Mono.empty();
                });
    }

    @PutMapping("/{id}/data")
    @Operation(summary = "上传数据",
            description = "",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true,
                    content = @Content(
                            mediaType = "application/octet-stream",
                            schema = @Schema(type = "string", format = "binary")
                    )
            ))
    public Mono<Void> putObjectData(@PathVariable(name = "id") String id,
                                    ServerWebExchange exchange,
                                    AbstractOAuth2TokenAuthenticationToken principal) {
        return Mono.just(storageService.generatePut(id, 1000 * 60 * 5))
                .flatMap(s -> {
                    Logger.getGlobal().info(s);
                    exchange.getResponse().setStatusCode(HttpStatus.TEMPORARY_REDIRECT);
                    exchange.getResponse().getHeaders().add("Location", s);

                    BaseStorageObject origin = new BaseStorageObject();

                    Logger.getGlobal().info(exchange.getRequest().getHeaders().toString());
                    origin.setSize(exchange.getRequest().getHeaders().getContentLength());
                    origin.setType(exchange.getRequest().getHeaders().getContentType().getType());
                    origin.setName(exchange.getRequest().getHeaders().getContentDisposition().getFilename());
                    origin.setDescription(exchange.getRequest().getHeaders().getContentDisposition().toString());

                    return storageService.put(origin);
                });
    }
}
