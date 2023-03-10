package com.crcl.authentication.migration;

import com.crcl.authentication.configuration.props.Registration;
import com.crcl.authentication.domain.Client;
import com.crcl.authentication.helpers.MigrationHelper;
import com.crcl.authentication.utils.AppClientScopes;
import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import org.springframework.context.annotation.Profile;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

import java.util.List;
import java.util.Set;

import static org.springframework.security.oauth2.core.ClientAuthenticationMethod.CLIENT_SECRET_POST;

@Profile("!dev")
@ChangeLog
public class OAuth2ClientMigration {

    @ChangeSet(order = "001", id = "save_default_clients", author = "@chahir_chalouati")
    public void saveClients(final MigrationHelper migrationHelper) {
        migrationHelper
                .getSecurityProperties()
                .getRegistrations().forEach((key, registration) -> {
                    final Client client = buildClient(migrationHelper, registration);
                    migrationHelper.getClientRepository().save(client);
                });
    }

    @ChangeSet(order = "002", id = "save_system_clients", author = "@chahir_chalouati")
    public void saveSystemClient(final MigrationHelper migrationHelper) {
        final Client client = new Client()
                .setId("SYSTEM")
                .setClientId("SYSTEM")
                .setClientSecret(migrationHelper.getPasswordEncoder().encode("SYSTEM"))
                .setClientAuthenticationMethods(Set.of(CLIENT_SECRET_POST))
                .setAuthorizationGrantTypes(Set.of(AuthorizationGrantType.CLIENT_CREDENTIALS))
                .setRedirectUris(Set.of())
                .setScopes(AppClientScopes.UI_SCOPES);
        migrationHelper.getClientRepository().save(client);
    }

    private static Client buildClient(MigrationHelper migrationHelper, Registration registration) {
        return getClient(migrationHelper, registration);
    }

    protected static Client getClient(MigrationHelper migrationHelper, Registration registration) {
        final List<AuthorizationGrantType> grantTypes = registration.getGrantTypes().stream()
                .map(AuthorizationGrantType::new)
                .toList();

        final List<String> redirectUris = registration.getUris().stream()
                .map(s -> s.concat("/authorized"))
                .toList();

        return new Client()
                .setId(registration.getId())
                .setClientId(registration.getId())
                .setClientSecret(migrationHelper.getPasswordEncoder().encode("secret"))
                .setClientAuthenticationMethods(Set.of(CLIENT_SECRET_POST))
                .setAuthorizationGrantTypes(grantTypes)
                .setRedirectUris(redirectUris)
                .setScopes(registration.getScopes());
    }
}
