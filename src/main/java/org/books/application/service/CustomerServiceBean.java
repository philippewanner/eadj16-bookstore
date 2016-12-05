/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.application.service;

import org.books.application.dto.Registration;
import org.books.application.exception.CustomerAlreadyExistsException;
import org.books.application.exception.CustomerNotFoundException;
import org.books.application.exception.InvalidPasswordException;
import org.books.persistence.dto.CustomerInfo;
import org.books.persistence.entity.Customer;
import org.books.persistence.repository.CustomerRepository;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.List;

/**
 * @author jan
 */
@Stateless(name = "CustomerService")
public class CustomerServiceBean extends AbstractService implements CustomerService {

	@EJB
	private CustomerRepository customerRepository;

	@Override
	public void authenticateCustomer(String email, String password)
			throws CustomerNotFoundException, InvalidPasswordException {
		logInfo("authenticateCustomer");
		throw new UnsupportedOperationException(
				"Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void changePassword(String email, String password) throws CustomerNotFoundException {
		logInfo("changePassword");
		throw new UnsupportedOperationException(
				"Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public Customer findCustomer(Long customerNr) throws CustomerNotFoundException {
		logInfo("findCustomer(Long customerNr)");
		throw new UnsupportedOperationException(
				"Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public Customer findCustomer(String email) throws CustomerNotFoundException {
		logInfo("findCustomer(String email)");
		throw new UnsupportedOperationException(
				"Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public Long registerCustomer(Registration registration) throws CustomerAlreadyExistsException {
		logInfo("registerCustomer");
		customerRepository.findInfosByName("Muster");
		return 0L;
	}

	@Override
	public List<CustomerInfo> searchCustomers(String name) {
		logInfo("searchCustomers");
		throw new UnsupportedOperationException(
				"Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void updateCustomer(Customer customer) throws CustomerNotFoundException, CustomerAlreadyExistsException {
		logInfo("updateCustomer");
		throw new UnsupportedOperationException(
				"Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
}