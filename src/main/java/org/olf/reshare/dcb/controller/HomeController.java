package org.olf.reshare.dcb.controller;
import java.util.HashMap;
import java.util.Map;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
@Controller
@Secured(SecurityRule.IS_ANONYMOUS)
public class HomeController {

	@Secured(SecurityRule.IS_ANONYMOUS)
	@Get
	public Map<String, Object> index() {
		return new HashMap<>();
	}

	@Secured(SecurityRule.IS_ANONYMOUS)
	@Post
	public Map<String, Object> indexPost() {
		return new HashMap<>();
	}

}