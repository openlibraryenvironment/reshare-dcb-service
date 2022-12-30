package org.olf.reshare.dcb.core;

import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.TypeRuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import graphql.schema.idl.errors.SchemaMissingError;
import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import io.micronaut.core.io.ResourceResolver;
import jakarta.inject.Singleton;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.olf.reshare.dcb.directory.beans.DirectoryDataFetchers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



@Factory 
public class GraphQLFactory {

    public static final Logger log = LoggerFactory.getLogger(GraphQLFactory.class);

    @Singleton 
    public GraphQL graphQL(ResourceResolver resourceResolver,
                           DirectoryDataFetchers directoryDataFetchers) {
  
	log.debug("GraphQLFactory::graphQL");

        SchemaParser schemaParser = new SchemaParser();
        SchemaGenerator schemaGenerator = new SchemaGenerator();

        // Load the schema
        InputStream schemaDefinition = resourceResolver
                .getResourceAsStream("classpath:schema.graphqls")
                .orElseThrow(SchemaMissingError::new);

        // Parse the schema and merge it into a type registry
        TypeDefinitionRegistry typeRegistry = new TypeDefinitionRegistry();
        typeRegistry.merge(schemaParser.parse(new BufferedReader(new InputStreamReader(schemaDefinition))));

	log.debug("Add runtime wiring");

        // Create the runtime wiring.
        RuntimeWiring runtimeWiring = RuntimeWiring.newRuntimeWiring()
                .type( TypeRuntimeWiring.newTypeWiring("Query")
                        .dataFetcher("agencies", directoryDataFetchers.getAgenciesDataFetcher())
                        .dataFetcher("hello", directoryDataFetchers.getHelloDataFetcher())
                     )

                 /*
                .type("Agency", typeWiring -> typeWiring  
                        .dataFetcher("agency", agencyDataFetcher))

                .type("Mutation", typeWiring -> typeWiring 
                        .dataFetcher("createToDo", createToDoDataFetcher)
                        .dataFetcher("completeToDo", completeToDoDataFetcher))

                .type("ToDo", typeWiring -> typeWiring 
                        .dataFetcher("author", authorDataFetcher))
                */

                .build();

        // Create the executable schema.
        GraphQLSchema graphQLSchema = schemaGenerator.makeExecutableSchema(typeRegistry, runtimeWiring);

        log.debug("returning {}",graphQLSchema.toString());

        // Return the GraphQL bean.
        return GraphQL.newGraphQL(graphQLSchema).build();
    }
}
