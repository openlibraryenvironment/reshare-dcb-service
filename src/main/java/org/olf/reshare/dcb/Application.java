package org.olf.reshare.dcb;

import io.micronaut.runtime.Micronaut;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.micronaut.serde.annotation.SerdeImport;

@SerdeImport(io.micronaut.configuration.graphql.ws.GraphQLWsResponse.class)
@SerdeImport(io.micronaut.configuration.graphql.GraphQLRequestBody.class)
@SerdeImport(io.micronaut.configuration.graphql.GraphQLResponseBody.class)
@OpenAPIDefinition(info = @Info(title = "DCB", description = "Direct Consortial Borrowing", version = "1.0.0"))
public class Application {
	public static void main(String[] args) {
		Micronaut.run(Application.class, args);
	}
}
