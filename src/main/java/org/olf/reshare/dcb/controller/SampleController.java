package org.olf.reshare.dcb.controller;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;


import io.micronaut.security.rules.SecurityRule;

@Controller("/secure")
@Secured(SecurityRule.IS_AUTHENTICATED)
public class SampleController {

	@Get("/admin")
	@Secured({"admin"})
	public String admin() {
		return "You are admin!";
	}

	@Get("/view")
	@Secured({"viewer"})
	public String view() {
		return "You are viewer!";
	}

	@Get("/anonymous")
	@Secured(SecurityRule.IS_ANONYMOUS)
	public String anonymous() {
		return "You are anonymous!";
	}
}