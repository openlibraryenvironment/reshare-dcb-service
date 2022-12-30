package org.olf.reshare.dcb.directory.storage;

import java.util.UUID;
import org.olf.reshare.dcb.directory.model.Agency;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.reactivestreams.Publisher;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.async.annotation.SingleResult;

/**
 * @See https://micronaut-projects.github.io/micronaut-r2dbc/snapshot/guide/index.html
 */
public interface AgencyRepository {

        @NonNull
        @SingleResult
        Publisher<Agency> findById(@NonNull UUID id);

        @NonNull
        Publisher<Agency> findAll();

        @NonNull
        @SingleResult
        Publisher<Boolean> existsById(@NonNull UUID id);

        public default void cleanUp() {
        };

        public default void commit() {
        }
}

