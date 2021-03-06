package cn.dustlight.storaging.application.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import cn.dustlight.auth.client.reactive.ReactiveAuthClient;
import cn.dustlight.auth.resources.AuthPrincipalUtil;
import cn.dustlight.auth.resources.core.AuthPrincipal;
import cn.dustlight.storaging.core.services.ConfigurationService;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/v1/configs")
@SecurityRequirement(name = "auth")
@Tag(name = "Configurations", description = "配置服务，提供 Key-Value 的存储。")
@CrossOrigin
public class ConfigurationController {

    @Autowired
    private ConfigurationService configurationService;

    private Log logger = LogFactory.getLog(getClass());

    @Operation(summary = "获取配置", description = "获取应用的用户或系统的配置。")
    @GetMapping("/{name}")
    public Mono<Map<String, ?>> getConfiguration(@PathVariable(name = "name") String name,
                                                 @RequestParam(name = "cid", required = false) String clientId,
                                                 @RequestParam(name = "system", required = false) boolean system,
                                                 ReactiveAuthClient reactiveAuthClient,
                                                 AuthPrincipal principal) {
        Mono<String> obtainClientId = system ?
                AuthPrincipalUtil.obtainClientIdRequireMember(reactiveAuthClient, clientId, principal) :
                AuthPrincipalUtil.obtainClientId(reactiveAuthClient, clientId, principal);
        return obtainClientId
                .flatMap(cid -> configurationService.getConfiguration(cid,
                        system ? null : principal.getUidString(),
                        name));
    }

    @Operation(summary = "设置配置", description = "新增或更新用户或系统配置。")
    @PutMapping("/{name}")
    public Mono<Void> setConfiguration(@PathVariable(name = "name") String name,
                                       @RequestBody Map<String, ?> config,
                                       @RequestParam(name = "cid", required = false) String clientId,
                                       @RequestParam(name = "system", required = false) boolean system,
                                       ReactiveAuthClient reactiveAuthClient,
                                       AuthPrincipal principal) {
        Mono<String> obtainClientId = system ?
                AuthPrincipalUtil.obtainClientIdRequireMember(reactiveAuthClient, clientId, principal) :
                AuthPrincipalUtil.obtainClientId(reactiveAuthClient, clientId, principal);
        return AuthPrincipalUtil.obtainClientId(reactiveAuthClient, clientId, principal)
                .flatMap(cid -> configurationService.setConfiguration(cid,
                        system ? null : principal.getUidString(),
                        name,
                        config));
    }
}
