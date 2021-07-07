package plus.storage.application.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import plus.auth.resources.AuthSecurityWebFilterChainConfiguration;

@EnableReactiveMethodSecurity
@Configuration
public class SecurityConfig extends AuthSecurityWebFilterChainConfiguration {
}
