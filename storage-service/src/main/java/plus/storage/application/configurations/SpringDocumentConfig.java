package plus.storage.application.configurations;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "对象存储服务",
                description = "提供文件、配置等对象存储服务。",
                contact = @Contact(
                        name = "Hansin",
                        email = "hansin@goodvoice.com"
                )
        )

)
@SecuritySchemes(value = @SecurityScheme(name = "auth",
        type = SecuritySchemeType.OAUTH2,
        in= SecuritySchemeIn.HEADER,
        scheme = "Bearer",
        flows =  @OAuthFlows(
                implicit = @OAuthFlow(
                        authorizationUrl="http://accounts.wgv/authorize")
        )
)
)
public class SpringDocumentConfig {
}
