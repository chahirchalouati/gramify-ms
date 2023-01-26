package com.crcl.post.configuration;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.spel.spi.EvaluationContextExtension;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

public class PrincipalResolver implements EvaluationContextExtension {

    @Override
    public @NotNull String getExtensionId() {
        return "oauth";
    }

    @Override
    public SecurityExpressionRoot getRootObject() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return new SecurityExpressionRoot(authentication) {
            @Override
            public Object getPrincipal() {
                Jwt jwt = (Jwt) super.getPrincipal();
                return (String) jwt.getClaims().get("username");
            }
        };
    }
}
