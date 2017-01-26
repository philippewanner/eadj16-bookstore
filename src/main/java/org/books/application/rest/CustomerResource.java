package org.books.application.rest;

import org.books.application.exception.CustomerAlreadyExistsException;
import org.books.application.exception.CustomerNotFoundException;
import org.books.application.service.CustomerService;
import org.books.application.service.CustomerServiceBean;
import org.books.persistence.dto.CustomerInfo;
import org.books.persistence.entity.Customer;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;

@Path("customers")
public class CustomerResource {

	@EJB
	private CustomerService service;

	@GET
	@Produces({APPLICATION_JSON, APPLICATION_XML})
	public Customer findCustomerByNumber(@PathParam("number") long number) {
		try {
			return service.findCustomer(number);
		} catch (CustomerNotFoundException e) {
			throw new WebApplicationException("Customer not found", Response.Status.NOT_FOUND);
		}
	}

	@GET
        @Path("search")
	@Produces({APPLICATION_JSON, APPLICATION_XML})
	public List<CustomerInfo> findCustomersByName(@QueryParam("name") String name) {
		return service.searchCustomers(name);
	}

	@PUT
	@Consumes({APPLICATION_JSON, APPLICATION_XML})
	@Produces({APPLICATION_JSON, APPLICATION_XML})
	public Customer updateCustomer(@PathParam("number") long number, Customer customer) {
		if (customer == null) {
			throw new WebApplicationException(Response.Status.NO_CONTENT);
		}

		try {
			customer.setNumber(number);
			service.updateCustomer(customer);

			return customer;
		} catch (CustomerNotFoundException e) {
			throw new WebApplicationException("Customer not found", Response.Status.NOT_FOUND);
		} catch (CustomerAlreadyExistsException e) {
			throw new WebApplicationException("new email already used", Response.Status.CONFLICT);
		}
	}
}