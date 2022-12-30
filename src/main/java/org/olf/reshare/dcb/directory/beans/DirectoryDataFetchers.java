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

@Singleton
public class DirectoryDataFetchers {

  public static final Logger log = LoggerFactory.getLogger(DirectoryDataFetchers.class);

  // When we use org.olf.reshare.dcb.directory.storage.AgencyRepository instead of PostgresAgencyRepository
  // the save method is not available
  private final PostgresAgencyRepository agencyRepository;

  public DirectoryDataFetchers(PostgresAgencyRepository agencyRepository) {
    this.agencyRepository = agencyRepository;
  }


  public DataFetcher<Iterable<Agency>> getAgenciesDataFetcher() {
    return dataFetchingEnvironment -> {
      log.debug("AgenciesDataFetcher::get");
      // ArrayList<Agency> al = new ArrayList();
      // al.add(new Agency(java.util.UUID.randomUUID(), "Wibble1"));
      // al.add(new Agency(java.util.UUID.randomUUID(), "Wibble2"));
      // al.add(new Agency(java.util.UUID.randomUUID(), "Wibble3"));
      // log.debug("Returning {}",al);
      // return al;
  
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

  public DataFetcher<Agency> createAgencyDataFetcher() {
    return env -> {
      String name = env.getArgument("name");
      return Mono.from(agencyRepository.save( new Agency(UUID.randomUUID(),name))).block();
    };
  }
}

