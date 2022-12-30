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
public class AgenciesDataFetcher implements DataFetcher<Iterable<Agency>> {

  public static final Logger log = LoggerFactory.getLogger(AgenciesDataFetcher.class);

  private final AgencyRepository agencyRepository;

  public AgenciesDataFetcher(AgencyRepository agencyRepository) {
    this.agencyRepository = agencyRepository;
  }

  @Override
  public Iterable<Agency> get(DataFetchingEnvironment env) {
    log.debug("AgenciesDataFetcher::get");
    ArrayList<Agency> al = new ArrayList();
    al.add(new Agency(java.util.UUID.randomUUID(), "Wibble1"));
    al.add(new Agency(java.util.UUID.randomUUID(), "Wibble2"));
    al.add(new Agency(java.util.UUID.randomUUID(), "Wibble3"));

    log.debug("Returning {}",al);

    return al;
    // return Flux.from(agencyRepository.findAll()).toIterable();
  }
}

