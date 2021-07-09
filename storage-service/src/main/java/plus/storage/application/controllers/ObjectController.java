package plus.storage.application.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.server.resource.authentication.AbstractOAuth2TokenAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import plus.auth.resources.AuthPrincipalUtil;
import plus.auth.resources.core.AuthPrincipal;
import plus.storage.core.ErrorEnum;
import plus.storage.core.entities.BaseStorageObject;
import plus.storage.core.entities.StorageObject;
import plus.storage.core.services.UrlStorageService;
import reactor.core.publisher.Mono;

import java.util.Arrays;

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

        return storageService.exists(id)
                .flatMap(flag -> {
                    if((Boolean)flag != true)
                        ErrorEnum.OBJECT_NOT_FOUND.throwException();

                    BaseStorageObject tmp = new BaseStorageObject();
                    tmp.setId(id);
                    tmp.setName(object.getName());
                    tmp.setDescription(object.getDescription());
                    tmp.setAdditional(object.getAdditional());
                    tmp.setCanRead(object.getCanRead());
                    tmp.setCanWrite(object.getCanWrite());
                    tmp.setOwner(object.getOwner());

                    return storageService.put(tmp);
                });
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

        return storageService.exists(id)
                .flatMap(flag -> {
                    if ((Boolean) flag != true)
                        ErrorEnum.OBJECT_NOT_FOUND.throwException();
                    return storageService.generateGetUrl(id, 1000 * 60 * 5)
                            .flatMap(s -> {
                                exchange.getResponse().setStatusCode(HttpStatus.TEMPORARY_REDIRECT);
                                exchange.getResponse().getHeaders().add("Location", s.toString());
                                return Mono.empty();
                            });
                });
    }

    @PutMapping("/{id}/data")
    @Operation(summary = "上传数据",
            description = "",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE,
                            schema = @Schema(type = "file", format = "binary")
                    )
            ))
    public Mono<Void> putObjectData(@PathVariable(name = "id") String id,
                                    @RequestHeader(name = "Content-Disposition", defaultValue = "attachment") String contentDisposition,
                                    ServerWebExchange exchange,
                                    AbstractOAuth2TokenAuthenticationToken principal) {
        var headers = exchange.getRequest().getHeaders();
        return storageService.exists(id)
                .flatMap(flag -> {
                    if ((Boolean) flag != true)
                        ErrorEnum.OBJECT_NOT_FOUND.throwException();
                    return storageService.generatePut(id, 1000 * 60 * 5, headers)
                            .flatMap(s -> {
                                exchange.getResponse().setStatusCode(HttpStatus.TEMPORARY_REDIRECT);
                                exchange.getResponse().getHeaders().add("Location", s.toString());

                                BaseStorageObject origin = new BaseStorageObject();
                                origin.setId(id);

                                if (headers != null) {
                                    origin.setSize(headers.getContentLength());
                                    var contentType = headers.getContentType();
                                    if (contentType != null) {
                                        origin.setType(contentType.toString());
                                    } else {
                                        origin.setType("");
                                    }
                                }


                                return storageService.put(origin);
                            });
                });
    }
}
