package plus.storage.application.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.security.Principal;

@RestController
public class TestController {

    @PreAuthorize("hasRole('Root')")
    @GetMapping("/")
    public Mono<Object> index(Principal principal){
        return Mono.just(principal);
    }
}
