package org.olf.reshare.dcb.controller;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import io.micronaut.security.annotation.Secured;

import static io.micronaut.security.rules.SecurityRule.IS_AUTHENTICATED;

/**
 *
 * 
 */
@Controller("/secure")
@Secured(IS_AUTHENTICATED)
public class SampleController {

		@Get("/admin")
		@Secured({"admin"})
		@Produces
		public String admin() {
			return "{admin:true}";
		}
	
		@Get("/view")
		@Secured({"viewer"})
		@Produces
		public String view() {
			return "{admin: false}";
		}
}
