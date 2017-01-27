package org.books.application.rest;

import org.books.application.dto.Registration;
import org.books.application.exception.CustomerAlreadyExistsException;
import org.books.application.exception.CustomerNotFoundException;
import org.books.application.exception.InvalidPasswordException;
import org.books.application.service.CustomerServiceBean;
import org.books.persistence.entity.Customer;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.MediaType.*;

@Path("registrations")
public class RegistrationResource {

	@EJB
	private CustomerServiceBean service;

	@POST
	@Consumes({ APPLICATION_JSON, APPLICATION_XML })
	@Produces(TEXT_PLAIN)
	public Response registerCustomer(Registration registration) {
		try {
			long number = service.registerCustomer(registration);
			return Response.status(Response.Status.CREATED).entity(String.valueOf(number)).build();
		} catch (CustomerAlreadyExistsException e) {
			throw new WebApplicationException("email address already used", Response.Status.CONFLICT);
		}
	}

	@GET
	@Produces(TEXT_PLAIN)
	@Path("{email}")
	public Response authenticateCustomer(@HeaderParam("password") String password, @PathParam("email") String email) {
		try {
			Customer customer = service.findCustomer(email);
			if (password == null) {
				return Response.status(Response.Status.UNAUTHORIZED).build();
			}

			service.authenticateCustomer(email, password);

			return Response.status(Response.Status.OK).entity(String.valueOf(customer.getNumber())).build();
		} catch (CustomerNotFoundException e) {
			throw new WebApplicationException("Customer not found", Response.Status.NOT_FOUND);
		} catch (InvalidPasswordException e) {
			throw new WebApplicationException(Response.Status.UNAUTHORIZED);
		}
	}

	@PUT
	@Consumes(TEXT_PLAIN)
	@Produces(TEXT_PLAIN)
	@Path("{email}")
	public Response changePassword(@PathParam("email") String email, String password) {
		try {
			Customer customer = service.findCustomer(email);
			if (password == null || password.length() == 0) {
				return Response.status(Response.Status.NO_CONTENT).build();
			}

			service.changePassword(email, password);

			return Response.status(Response.Status.OK).entity(String.valueOf(customer.getNumber())).build();
		} catch (CustomerNotFoundException e) {
			throw new WebApplicationException("Customer not found", Response.Status.NOT_FOUND);
		}
	}
}