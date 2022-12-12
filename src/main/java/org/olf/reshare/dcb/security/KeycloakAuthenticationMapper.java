package org.olf.reshare.dcb.security;


import java.util.HashMap;
import java.util.Map;

import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oracle.svm.core.annotate.Inject;

import io.micronaut.context.annotation.Property;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.reactor.http.client.ReactorHttpClient;
import io.micronaut.security.authentication.AuthenticationResponse;
import io.micronaut.security.oauth2.endpoint.authorization.state.State;
import io.micronaut.security.oauth2.endpoint.token.response.OauthAuthenticationMapper;
import io.micronaut.security.oauth2.endpoint.token.response.TokenResponse;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import reactor.core.publisher.Flux;

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

	public static final String TOKEN_PREFIX = "token=";

	@Override
	public Publisher<AuthenticationResponse> createAuthenticationResponse(TokenResponse tokenResponse, @Nullable State state) {
		Flux<HttpResponse<KeycloakUser>> res = client
				.exchange(HttpRequest.POST("/realms/dcb/protocol/openid-connect/token/introspect",
				TOKEN_PREFIX + tokenResponse.getAccessToken())
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.basicAuth(clientId, clientSecret), KeycloakUser.class);
		return res.map(user -> {
			log.info("User: {}", user.body());
			Map<String, Object> attrs = new HashMap<>();
			attrs.put("Bearer", tokenResponse.getAccessToken());
			return AuthenticationResponse.success(user.body().getUsername(), user.body().getRoles(), attrs);
		});
	}
}
