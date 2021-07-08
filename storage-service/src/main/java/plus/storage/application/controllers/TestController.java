package plus.storage.application.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.*;
import java.security.Principal;
import java.util.List;
import java.util.function.Function;

@RestController
@SecurityRequirement(name = "auth")
public class TestController {

    Log logger = LogFactory.getLog(getClass());

    @PreAuthorize("hasRole('Root')")
    @GetMapping("/index")
    public Mono<Object> index(Principal principal){
        return Mono.just(principal);
    }

    @PutMapping("/put")
    @Operation(
            requestBody = @RequestBody(required = true,
                    content = @Content(
                            mediaType = "image/*",
                            schema = @Schema(type = "string", format = "binary")
                    )
            )
    )
    public Mono<Void> put(ServerWebExchange exchange) throws FileNotFoundException {
        FileOutputStream outputStream = new FileOutputStream(new File("a"));
        return exchange.getRequest().getBody()
                .filter(dataBuffer -> {
                    InputStream in = dataBuffer.asInputStream();
                    try {
                        outputStream.write(in.readAllBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return true;
                }).doFinally(dataBuffer -> {
                    try {
                        outputStream.flush();
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).then();
    }
}
