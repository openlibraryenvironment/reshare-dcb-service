package org.olf.reshare.dcb.directory.beans;

import io.micronaut.context.event.StartupEvent;
import io.micronaut.runtime.event.annotation.EventListener;
import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.inject.Singleton;

/**
 * Used at DCB instance startup to ensure any reference data related to the directory schema is present
 */
@Transactional 
@Singleton
public class DirectoryBootstrap {

  public static final Logger log = LoggerFactory.getLogger(DirectoryBootstrap.class);

  // @PersistenceContext private EntityManager entityManager;

  @EventListener
  protected void init(StartupEvent event) {
    log.debug("Bootstrap::init");
        // if no data yet in the database
        /*
        if (((Number) entityManager.createQuery("select count(*) from Genre").getSingleResult()).longValue() == 0) {
            logger.debug("Loading initial data to the database");

            Genre genre = new Genre("Crime");
            entityManager.persist(genre);

            entityManager.persist(new Book("Truman Capote", "In Cold Blood", genre));
            entityManager.persist(new Book("James Ellroy", "My Dark Places", genre));
            entityManager.persist(new Book("Ann Rule", "The Stranger Beside Me", genre));
        }

        // log what we have in the db
        for (Book book : (List<Book>) entityManager.createQuery("from Book").getResultList())
            logger.debug("Book found: {}, {}, {}", book.getAuthor(), book.getName(), book.getGenre().getName());
        */
  }
}

