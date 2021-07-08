package plus.storage.application.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.security.Principal;

@RestController
@SecurityRequirement(name = "auth")
public class TestController {

    @PreAuthorize("hasRole('Root')")
    @GetMapping("/index")
    public Mono<Object> index(Principal principal){
        return Mono.just(principal);
    }
}
