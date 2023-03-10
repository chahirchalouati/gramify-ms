package com.crcl.authentication.configuration.security;

import com.crcl.authentication.clients.SrvProfileClient;
import com.crcl.authentication.domain.Permission;
import com.crcl.authentication.domain.Role;
import com.crcl.authentication.domain.User;
import com.crcl.authentication.utils.ProfileUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.ACCESS_TOKEN;


public class TokenCustomizer implements OAuth2TokenCustomizer<JwtEncodingContext> {

    private final SrvProfileClient profileClient;

    public TokenCustomizer(SrvProfileClient profileClient) {
        this.profileClient = profileClient;
    }

    @Override
    public void customize(JwtEncodingContext context) {
        final Authentication principal = context.getPrincipal();
        boolean isToken = Objects.equals(context.getTokenType().getValue(), ACCESS_TOKEN);
        if (isToken && principal instanceof UsernamePasswordAuthenticationToken) {
            final var user = (User) principal.getPrincipal();
            final var profile = Optional.ofNullable(profileClient.findByUsername(user.getUsername()))
                    .orElse(ProfileUtils.getDefaultProfile(user));

            context.getClaims()
                    .claim("email", user.getEmail())
                    .claim("username", user.getUsername())
                    .claim("firstName", user.getFirstName())
                    .claim("lastName", user.getLastName())
                    .claim("email", user.getLastName())
                    .claim("roles", getAuthorities(user.getRoles()))
                    .claim("profile", profile);
        }
    }

    public Set<String> getAuthorities(Set<Role> roles) {
        final Stream<String> permissionsStream = roles.stream()
                .map(Role::getPermissions)
                .flatMap(Set::stream)
                .map(Permission::getName);

        final Stream<String> rolesStream = roles.stream()
                .map(Role::getName);

        return Stream.concat(permissionsStream, rolesStream)
                .collect(Collectors.toSet());
    }


}
