package com.crcl.authentication.migration;

import com.crcl.authentication.domain.Client;
import com.crcl.authentication.service.MigrationHelper;
import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

import java.util.List;
import java.util.Set;

import static org.springframework.security.oauth2.core.ClientAuthenticationMethod.CLIENT_SECRET_POST;

@ChangeLog
public class ClientChangeLog {

    @ChangeSet(order = "001", id = "save_default_clients", author = "@chahir_chalouati")
    public void saveClients(final MigrationHelper migrationHelper) {
        migrationHelper
                .getSecurityProperties()
                .getRegistrations().forEach((key, registration) -> {
                    final List<AuthorizationGrantType> grantTypes = registration.getGrantTypes().stream()
                            .map(AuthorizationGrantType::new)
                            .toList();
                    final List<String> redirectUris = registration.getUris().stream()
                            .map(s -> s.concat("/authorized"))
                            .toList();
                    final Client client = new Client()
                            .setId(registration.getId())
                            .setClientId(registration.getId())
                            .setClientSecret(migrationHelper.getPasswordEncoder().encode("secret"))
                            .setClientAuthenticationMethods(Set.of(CLIENT_SECRET_POST))
                            .setAuthorizationGrantTypes(grantTypes)
                            .setRedirectUris(redirectUris)
                            .setScopes(registration.getScopes());
                    migrationHelper.getClientRepository().save(client);
                });
    }
}
