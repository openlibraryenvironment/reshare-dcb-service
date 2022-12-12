package org.olf.reshare.dcb;

import com.nimbusds.jwt.JWTParser;
import com.nimbusds.jwt.SignedJWT;
import io.micronaut.security.authentication.UsernamePasswordCredentials;
import io.micronaut.security.token.jwt.render.AccessRefreshToken;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest
class DeclarativeHttpClientWithJwtTest {

	// @Test
	// void verifyJwtAuthenticationWorksWithDeclarativeClient ()
	//     throws ParseException {
	// 	UsernamePasswordCredentials creds = new UsernamePasswordCredentials("user","password");
	// 	AccessRefreshToken loginRsp = apiClient.getUser("token " + tokenResponse.getAccessToken());

	// 	assertNotNull(loginRsp);
	// 	assertNotNull(loginRsp.getAccessToken());
	// 	assertTrue(JWTParser.parse(loginRsp.getAccessToken()) instanceof SignedJWT);

	// 	String msg = apiClient.getUser(null);
	// 	assertEquals("user", msg);
	// }
}