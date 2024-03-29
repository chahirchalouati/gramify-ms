package com.crcl.processor.configuration.security;

import com.crcl.common.configuration.SwaggerConfiguration;
import com.crcl.common.properties.ApiProperties;
import com.crcl.common.utils.EndpointsUtils;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@AllArgsConstructor
@Import({ApiProperties.class, SwaggerConfiguration.class})
public class ResourceServerConfig {
    private final CorsCustomizer corsCustomizer;

    @Bean
    public SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http) {
        corsCustomizer.corsCustomizer(http);
        http.csrf().disable()
                .authorizeExchange()
                .pathMatchers(EndpointsUtils.Permitted.ACTUATOR_END_POINTS).permitAll()
                .pathMatchers("/utils/**").permitAll()
                .pathMatchers(EndpointsUtils.Permitted.SWAGGER_END_POINTS).permitAll()
                .anyExchange().authenticated()
                .and()
                .oauth2ResourceServer()
                .jwt();
        return http.build();
    }
}
