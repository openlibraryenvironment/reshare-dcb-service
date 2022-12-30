package org.olf.reshare.dcb.directory;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


import io.micronaut.core.type.Argument;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.runtime.EmbeddedApplication;
import jakarta.inject.Inject;
import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.olf.reshare.dcb.directory.storage.postgres.PostgresAgencyRepository;
import org.olf.reshare.dcb.directory.model.Agency;
import java.util.UUID;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.List;
import java.util.Map;

@MicronautTest(transactional = false)
class DirectoryTest {

	public static final Logger log = LoggerFactory.getLogger(DirectoryTest.class);


	@Inject
	PostgresAgencyRepository agency_repository;

	@Inject
	EmbeddedApplication<?> application;

	@Inject
	@Client("/")
	HttpClient client;


	void setupSpec(){
		log.debug("DirectoryTest::setupSpec");
	}


	@Transactional
	void cleanup(){
		log.debug("DirectoryTest::cleanup");
	}

        /**
         * Test agency creation but also provide a signpost for how to deal with some reactive use cases
         * @See also https://micronaut-projects.github.io/micronaut-r2dbc/snapshot/guide/index.html
         */
	@Test
	void testAgencyCreation () {
		log.debug("Directory lifecycle test");

		Mono<Agency> uos_mono = Mono.from(agency_repository.save( new Agency(UUID.randomUUID(),"University of Sheffield")));
		log.debug("Created uos: {}",uos_mono.block().toString());

		Agency stl_instance = Mono.from(agency_repository.save( new Agency(UUID.randomUUID(),"St Louis University"))).block();
		log.debug("Created stl: {}",stl_instance);

                // See id the repo also gives os old style imperative api
		Agency diku = Mono.from(agency_repository.save( new Agency(UUID.randomUUID(),"diku Snapshot"))).block();
		log.debug("Created diku: {}",diku.toString());

		// Make sure that we have 3 agencies
                List<Agency> agencies = Flux.from(agency_repository.findAll()).collectList().block();
                assert agencies.size() == 3;
	}

	@Test
        void testPublicHome() {
        	HttpRequest<String> request = HttpRequest.GET("/");
		String body = client.toBlocking().retrieve(request);
		log.debug("public home result: {}",body);
        }

        @Test
        void testGraphQLQueryForAgencies() {
		// see https://guides.micronaut.io/latest/micronaut-graphql-todo-maven-java.html
                log.debug("Request agencies");
		List<Map> agencies = getAgencies();
		log.debug("Got result {}",agencies.toString());
		assert agencies.size() > 0;
        }

        @Test
        void testGraphQLHello() {
		String query = "{\"query\":\"{ hello(name:'testname') }\"}";
		HttpRequest<String> request = HttpRequest.POST("/graphql", query);
		HttpResponse<String> response = client.toBlocking().exchange(request, Argument.of(String.class));
		log.debug("Hello response: {}",response.body());
        }


	private HttpResponse<Map> fetch(String query) {
		HttpRequest<String> request = HttpRequest.POST("/graphql", query);
		HttpResponse<Map> response = client.toBlocking().exchange(request, Argument.of(Map.class));
		assert response.status() == HttpStatus.OK;
		assert response.body() != null;
		return response;
	}

	private List<Map> getAgencies() {
		String query = "{\"query\":\"{ agencies { name } }\"}";
		HttpResponse<Map> response = fetch(query);
		return (List<Map>) ((Map) response.getBody().get().get("data")).get("toDos");
	}
}