package org.olf.reshare.dcb.directory.beans;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.olf.reshare.dcb.directory.storage.AgencyRepository;
import org.olf.reshare.dcb.directory.model.Agency;
import reactor.core.publisher.Flux;

import java.util.ArrayList;

@Singleton
public class DirectoryDataFetchers {

  public static final Logger log = LoggerFactory.getLogger(DirectoryDataFetchers.class);

  private final AgencyRepository agencyRepository;

  public DirectoryDataFetchers(AgencyRepository agencyRepository) {
    this.agencyRepository = agencyRepository;
  }


  public DataFetcher<Iterable<Agency>> getAgenciesDataFetcher() {
    return dataFetchingEnvironment -> { // <2>
      log.debug("AgenciesDataFetcher::get");
      ArrayList<Agency> al = new ArrayList();
      al.add(new Agency(java.util.UUID.randomUUID(), "Wibble1"));
      al.add(new Agency(java.util.UUID.randomUUID(), "Wibble2"));
      al.add(new Agency(java.util.UUID.randomUUID(), "Wibble3"));
  
      log.debug("Returning {}",al);
  
      // return Flux.from(agencyRepository.findAll()).toIterable();
      return al;
    };
  }

  public DataFetcher<String> getHelloDataFetcher() {
    return dataFetchingEnvironment -> { // <2>
      String name = dataFetchingEnvironment.getArgument("name");
      if (name == null || name.trim().length() == 0) {
        name = "World";
      }
      return String.format("Hello %s!", name);
    };
  }

}

