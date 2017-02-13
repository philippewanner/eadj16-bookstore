package org.books.application.rest;

import java.io.IOException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;

public class CORSFilter implements ContainerResponseFilter {

	@Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
		responseContext.getHeaders().putSingle("Access-Control-Allow-Origin", "*");
		responseContext.getHeaders().putSingle("Access-Control-Allow-Credentials", "true");
		responseContext.getHeaders().putSingle("Access-Control-Allow-Methods", "HEAD, GET, POST, DELETE, PUT");
		responseContext.getHeaders().putSingle("Access-Control-Allow-Headers", "Content-Type, Accept, Authorization, password");
	}
}
