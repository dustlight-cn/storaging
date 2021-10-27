package plus.storage.application.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import plus.auth.client.reactive.ReactiveAuthClient;
import plus.auth.resources.AuthPrincipalUtil;
import plus.auth.resources.core.AuthPrincipal;
import plus.storage.core.ErrorEnum;
import plus.storage.core.entities.BaseStorageObject;
import plus.storage.core.entities.QueryResult;
import plus.storage.core.entities.StorageObject;
import plus.storage.core.services.UrlStorageService;
import reactor.core.publisher.Mono;

import java.util.Arrays;

@RestController
@RequestMapping("/v1/objects")
@SecurityRequirement(name = "auth")
@Tag(name = "Objects", description = "对象存储服务，提供 Key-Value 的对象存储。")
@CrossOrigin
public class ObjectController {

    @Autowired
    private UrlStorageService storageService;

    @Operation(summary = "创建对象", description = "创建一个对象。")
    @PostMapping("")
    public Mono<StorageObject> createObject(@RequestBody BaseStorageObject object,
                                            @RequestParam(name = "cid", required = false) String clientId,
                                            ReactiveAuthClient reactiveAuthClient,
                                            AuthPrincipal principal) {
        return AuthPrincipalUtil.obtainClientId(reactiveAuthClient, clientId, principal)
                .flatMap(cid -> {
                    BaseStorageObject origin = new BaseStorageObject();
                    origin.setClientId(cid);
                    origin.setOwner(object.getOwner());
                    origin.setCanRead(object.getCanRead());
                    origin.setCanWrite(object.getCanWrite());
                    origin.setName(object.getName());
                    origin.setDescription(object.getDescription());
                    if (StringUtils.hasText(principal.getUidString()))
                        if (origin.getOwner() == null) {
                            origin.setOwner(Arrays.asList(principal.getUidString()));
                        } else {
                            origin.getOwner().add(principal.getUidString());
                        }
                    return storageService.create(origin);
                });
    }

    @Operation(summary = "获取对象", description = "获取指定对象。")
    @GetMapping("/{id}")
    public Mono<StorageObject> getObject(@PathVariable(name = "id") String id,
                                         @RequestParam(name = "cid", required = false) String clientId,
                                         ReactiveAuthClient reactiveAuthClient,
                                         AuthPrincipal principal) {
        return AuthPrincipalUtil.obtainClientId(reactiveAuthClient, clientId, principal)
                .flatMap(cid -> storageService.get(id));
    }

    @Operation(summary = "更新对象", description = "更新指定对象。")
    @PutMapping("/{id}")
    public Mono<Void> putObject(@PathVariable(name = "id") String id,
                                @RequestBody BaseStorageObject object,
                                @RequestParam(name = "cid", required = false) String clientId,
                                ReactiveAuthClient reactiveAuthClient,
                                AuthPrincipal principal) {
        return AuthPrincipalUtil.obtainClientId(reactiveAuthClient, clientId, principal)
                .flatMap(cid -> storageService.exists(id))
                .flatMap(flag -> {
                    if ((Boolean) flag != true)
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

    @Operation(summary = "删除对象", description = "删除指定对象。")
    @DeleteMapping("/{id}")
    public Mono<Void> deleteObject(@PathVariable(name = "id") String id,
                                   @RequestParam(name = "cid", required = false) String clientId,
                                   ReactiveAuthClient reactiveAuthClient,
                                   AuthPrincipal principal) {
        return AuthPrincipalUtil.obtainClientId(reactiveAuthClient, clientId, principal)
                .flatMap(cid -> storageService.delete(id));
    }

    @Operation(summary = "获取对象数据", description = "获取对象的数据。")
    @GetMapping("/{id}/data")
    public Mono<Void> getObjectData(@PathVariable(name = "id") String id,
                                    ServerWebExchange exchange,
                                    @RequestParam(name = "cid", required = false) String clientId,
                                    ReactiveAuthClient reactiveAuthClient,
                                    AuthPrincipal principal) {
        return AuthPrincipalUtil.obtainClientId(reactiveAuthClient, clientId, principal)
                .flatMap(cid -> storageService.exists(id))
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
    @Operation(summary = "更新对象数据",
            description = "更新对象的数据。",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE,
                            schema = @Schema(type = "file", format = "binary")
                    )
            ))
    public Mono<Void> putObjectData(@PathVariable(name = "id") String id,
                                    @RequestHeader(name = "Content-Disposition", defaultValue = "attachment") String contentDisposition,
                                    ServerWebExchange exchange,
                                    @RequestParam(name = "cid", required = false) String clientId,
                                    ReactiveAuthClient reactiveAuthClient,
                                    AuthPrincipal principal) {
        return AuthPrincipalUtil.obtainClientId(reactiveAuthClient, clientId, principal)
                .flatMap(cid -> storageService.exists(id)
                        .flatMap(flag -> {
                            if ((Boolean) flag != true)
                                ErrorEnum.OBJECT_NOT_FOUND.throwException();
                            var headers = exchange.getRequest().getHeaders();
                            if (headers == null)
                                headers = new HttpHeaders();
                            headers.add("Content-Disposition", contentDisposition);

                            BaseStorageObject origin = new BaseStorageObject();
                            origin.setId(id);

                            origin.setSize(headers.getContentLength());
                            var contentType = headers.getContentType();
                            if (contentType != null) {
                                origin.setType(contentType.toString());
                            } else {
                                origin.setType("");
                            }

                            return storageService.generatePut(id, 1000 * 60 * 5, headers)
                                    .flatMap(s -> {
                                        exchange.getResponse().setStatusCode(HttpStatus.TEMPORARY_REDIRECT);
                                        exchange.getResponse().getHeaders().add("Location", s.toString());
                                        return storageService.put(origin);
                                    });
                        })
                );
    }

    @Operation(summary = "查找对象", description = "查找对象的数据。")
    @GetMapping("")
    public Mono<QueryResult<StorageObject>> findObjects(@RequestParam(name = "q", required = false) String keywords,
                                                        @RequestParam(name = "page", defaultValue = "0") int page,
                                                        @RequestParam(name = "size", defaultValue = "10") int size,
                                                        @RequestParam(name = "cid", required = false) String clientId,
                                                        ReactiveAuthClient reactiveAuthClient,
                                                        AuthPrincipal principal) {
        return AuthPrincipalUtil.obtainClientId(reactiveAuthClient, clientId, principal)
                .flatMap(cid -> storageService.find(keywords, page, size, cid, principal.getUidString()));
    }
}
