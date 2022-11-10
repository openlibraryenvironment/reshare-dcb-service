package org.olf.reshare.dcb.gokb.bib;

import java.util.List;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record GokbTipp(
		@Nullable String tippTitleName,
		@Nullable String titleType,
		List<GokbIdentifier> identifiers) {
}