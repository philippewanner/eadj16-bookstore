package org.books.application.rest;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("rest")
public class ApplicationConfig extends Application {

	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> classes = new HashSet<>();
		classes.add(CatalogResource.class);
		classes.add(CustomerResource.class);
		classes.add(OrderResource.class);
		classes.add(RegistrationResource.class);
		classes.add(CORSFilter.class);
		return classes;
	}
}