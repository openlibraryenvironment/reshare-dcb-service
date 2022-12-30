package org.olf.reshare.dcb.core;

import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
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

import org.olf.reshare.dcb.directory.beans.AgenciesDataFetcher;
import org.olf.reshare.dcb.directory.beans.HelloDataFetcher;

@Factory 
public class GraphQLFactory {

    @Singleton 
    public GraphQL graphQL(ResourceResolver resourceResolver,
                           HelloDataFetcher helloDataFetcher,
                           AgenciesDataFetcher agenciesDataFetcher) {
        SchemaParser schemaParser = new SchemaParser();
        SchemaGenerator schemaGenerator = new SchemaGenerator();

        // Load the schema
        InputStream schemaDefinition = resourceResolver
                .getResourceAsStream("classpath:schema.graphqls")
                .orElseThrow(SchemaMissingError::new);

        // Parse the schema and merge it into a type registry
        TypeDefinitionRegistry typeRegistry = new TypeDefinitionRegistry();
        typeRegistry.merge(schemaParser.parse(new BufferedReader(new InputStreamReader(schemaDefinition))));

        // Create the runtime wiring.
        RuntimeWiring runtimeWiring = RuntimeWiring.newRuntimeWiring()
                .type("Query", typeWiring -> typeWiring  
                        .dataFetcher("agencies", agenciesDataFetcher)
                        .dataFetcher("hello", helloDataFetcher)
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

        // Return the GraphQL bean.
        return GraphQL.newGraphQL(graphQLSchema).build();
    }
}
