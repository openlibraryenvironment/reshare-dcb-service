package org.olf.reshare.dcb.security;


import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.reactor.http.client.ReactorHttpClient;
import io.micronaut.security.authentication.AuthenticationResponse;
import io.micronaut.security.oauth2.endpoint.authorization.state.State;
import io.micronaut.security.oauth2.endpoint.token.response.OauthAuthenticationMapper;
import io.micronaut.security.oauth2.endpoint.token.response.TokenResponse;
import io.micronaut.context.annotation.Property;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.async.publisher.Publishers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.reactivestreams.Publisher;

import com.oracle.svm.core.annotate.Inject;

import jakarta.inject.Named;
import jakarta.inject.Singleton;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Named("keycloak")
@Singleton
class KeycloakAuthenticationMapper implements OauthAuthenticationMapper {

	@Property(name = "micronaut.security.oauth2.clients.keycloak.client-id")
	private String clientId;
	
	@Property(name = "micronaut.security.oauth2.clients.keycloak.client-secret")
	private String clientSecret;

	private static Logger log = LoggerFactory.getLogger(KeycloakAuthenticationMapper.class);

	@Client("http://localhost:8081")
	@Inject
	private ReactorHttpClient client;

	//public static final String TOKEN_PREFIX = "token ";

	@Override
	public Publisher<AuthenticationResponse> createAuthenticationResponse(TokenResponse tokenResponse, @Nullable State state) {
		return Flux.from(client
		            .exchange(HttpRequest.GET("/realms/dcb/protocol/openid-connect/token")
		            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
		            .basicAuth(clientId, clientSecret), KeycloakUser.class))
					.map(user -> {
									log.info("User: {}", user.body());
									Map<String, Object> attrs = new HashMap<>();
									attrs.put("openIdToken", tokenResponse.getAccessToken());
									return AuthenticationResponse.success(user.body().getUsername(), user.body().getRoles(), attrs);
								});
	}
}