package org.olf.reshare.dcb.directory.model;

import io.micronaut.data.annotation.*;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import io.micronaut.core.annotation.NonNull;
import jakarta.persistence.Column;
import io.micronaut.serde.annotation.Serdeable;
import io.micronaut.core.annotation.Introspected;



/**
 * Agency.
 * An Agency in the NCIP sense of the word - An Agency is a more general term for an actor in a library context. Institutions
 * are agencies (N.B. the Institution itself, and not it's branch libraries, head office, locations, collections or other sub-structures).
 * Intermediaries can also be agencies, as can suppliers. Agencies are closely aligned to the directory structure of an "Institution" but
 * with additional constraints that they must support API features.
 * An Agency is always hosted by 1 "LocalServer". A "LocalServer" may host many Agencies.
 * In MOBIUS for example, the kc-towers "LocalServer" hosts 20 Agencies including Rockhurst.
 */
@MappedEntity
@Introspected  // because https://guides.micronaut.io/latest/micronaut-graphql-gradle-java.html told me to
public class Agency {

  @NotNull
  @NonNull
  @Id
  @Column(columnDefinition = "UUID")
  private UUID id;

  @NotNull
  @NonNull
  @Column(columnDefinition = "TEXT")
  private String name;


  public Agency(UUID id, String name) {
    this.id = id;
    this.name = name;
  }

  public Agency(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public UUID getId() {
    return id;
  }

  public String toString() {
    return String.format("[%s] %s",id,name);
  }
}
