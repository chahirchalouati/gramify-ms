package com.crcl.notification.configuration.Oauth2;

import com.crcl.common.utils.EndpointsUtils;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
@AllArgsConstructor
public class ResourceServerConfig {
    private static final String ACTUATOR_ENDPOINT_PATTERN = "/actuator/*";
    private final CorsCustomizer corsCustomizer;

    @Bean
    public SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http) {
        corsCustomizer.corsCustomizer(http);
        http.csrf().disable()
                .authorizeExchange()
                .pathMatchers(ACTUATOR_ENDPOINT_PATTERN).permitAll()
                .pathMatchers(EndpointsUtils.Permitted.SWAGGER_END_POINTS).permitAll()
                .anyExchange().authenticated()
                .and()
                .oauth2ResourceServer()
                .jwt();
        return http.build();
    }
}
