package org.olf.reshare.dcb.directory.beans;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.olf.reshare.dcb.directory.storage.AgencyRepository;
import org.olf.reshare.dcb.directory.storage.postgres.PostgresAgencyRepository;
import org.olf.reshare.dcb.directory.model.Agency;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.ArrayList;
import java.util.UUID;
import io.micronaut.security.utils.SecurityService;

@Singleton
public class DirectoryDataFetchers {

  public static final Logger log = LoggerFactory.getLogger(DirectoryDataFetchers.class);

  // When we use org.olf.reshare.dcb.directory.storage.AgencyRepository instead of PostgresAgencyRepository
  // the save method is not available
  private final PostgresAgencyRepository agencyRepository;
  private final SecurityService securityService;

  public DirectoryDataFetchers(PostgresAgencyRepository agencyRepository,
                               SecurityService securityService) {
    this.agencyRepository = agencyRepository;
    this.securityService = securityService;
  }


  /**
   * Retrieve agencies.
   * NO security constraints placed on this call (Except perhaps rate limiting later on)
   *
   * RequestResponse customizers - see here:
   * See here https://github.com/micronaut-projects/micronaut-graphql/blob/master/examples/jwt-security/src/main/java/example/graphql/RequestResponseCustomizer.java
   * 
   */
  public DataFetcher<Iterable<Agency>> getAgenciesDataFetcher() {
    return dataFetchingEnvironment -> {
      log.debug("AgenciesDataFetcher::get");

      // securityService...  boolean isAuthenticated(), boolean hasRole(String), Optional<Authentication> getAuthentication Optional<String> username
      log.debug("Current user : {}",securityService.username().orElse(null));
      return Flux.from(agencyRepository.findAll()).toIterable();
    };
  }

  public DataFetcher<String> getHelloDataFetcher() {
    return dataFetchingEnvironment -> {
      String name = dataFetchingEnvironment.getArgument("name");
      if (name == null || name.trim().length() == 0) {
        name = "World";
      }
      return String.format("Hello %s!", name);
    };
  }

  // Useful info https://www.graphql-java.com/documentation/execution/ on custom data fetcher exceptions
  public DataFetcher<Agency> createAgencyDataFetcher() {

    return env -> {
      if ( securityService.isAuthenticated() ) {
        log.debug("User authenticated as {} - proceed to create agency",securityService.username().orElse(null));
        String name = env.getArgument("name");
        return Mono.from(agencyRepository.save( new Agency(UUID.randomUUID(),name))).block();
      }
      else {
        log.debug("User NOT authenticated - unable to create agency");
      }
      
      return null;
    };
  }
}

