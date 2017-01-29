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
import org.books.persistence.entity.Login;
import org.books.persistence.enumeration.UserGroup;
import org.books.persistence.repository.CustomerRepository;
import org.books.persistence.repository.UserRepository;

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
	@EJB
	private UserRepository userRepository;

	@Override
	public void authenticateCustomer(String email, String password)
			throws CustomerNotFoundException, InvalidPasswordException {
		logInfo("authenticateCustomer");
		Login login = userRepository.findByUserName(email);
		if (login == null) {
			throw new CustomerNotFoundException();
		}

		if (!password.equals(login.getPassword())) {
			throw new InvalidPasswordException();
		}
	}

	@Override
	public void changePassword(String email, String password) throws CustomerNotFoundException {
		logInfo("changePassword");
		Login login = userRepository.findByUserName(email);
		if (login == null) {
			throw new CustomerNotFoundException();
		}

		login.setPassword(password);
	}

	@Override
	public Customer findCustomer(Long customerNr) throws CustomerNotFoundException {
		logInfo("findCustomer(Long customerNr)");
		Customer customer = customerRepository.find(customerNr);
		if (customer != null) {
			return customer;
		}
		throw new CustomerNotFoundException();
	}

	@Override
	public Customer findCustomer(String email) throws CustomerNotFoundException {
		logInfo("findCustomer(String email)");
		Customer customer = customerRepository.find(email);
		if (customer != null) {
			return customer;
		}
		throw new CustomerNotFoundException();
	}

	@Override
	public Long registerCustomer(Registration registration) throws CustomerAlreadyExistsException {
		logInfo("registerCustomer");
		Customer customer = registration.getCustomer();
		if (userRepository.findByUserName(customer.getEmail()) != null) {
			throw new CustomerAlreadyExistsException();
		}
		if (customerRepository.find(customer.getEmail()) != null) {
			throw new CustomerAlreadyExistsException();
		}

		Login login = new Login();
		login.setGroup(UserGroup.CUSTOMER);
		login.setUserName(customer.getEmail());
		login.setPassword(registration.getPassword());
		userRepository.persist(login);
		customerRepository.persist(customer);

		// Brauchen generierte nummer
		customerRepository.flush();

		return customer.getNumber();
	}

	@Override
	public List<CustomerInfo> searchCustomers(String name) {
		logInfo("searchCustomers");
		return customerRepository.search(name);
	}

	@Override
	public void updateCustomer(Customer customer) throws CustomerNotFoundException, CustomerAlreadyExistsException {
		logInfo("updateCustomer");
		if (customer.getNumber() == null) {
			throw new IllegalArgumentException("Number of customer must not be null");
		}

		Customer dbCustomer = customerRepository.find(customer.getNumber());
		if (dbCustomer == null) {
			throw new CustomerNotFoundException();
		}

		if (!dbCustomer.getEmail().equals(customer.getEmail())) {
			Customer c = customerRepository.find(customer.getEmail());
			if (c != null) {
				throw new CustomerAlreadyExistsException();
			}

			Login login = userRepository.findByUserName(customer.getEmail());
			if (login != null) {
				throw new CustomerAlreadyExistsException();
			}

			login = userRepository.findByUserName(dbCustomer.getEmail());
			if (login == null) {
				throw new CustomerNotFoundException();
			}

			login.setUserName(customer.getEmail());
		}

		customerRepository.update(customer);
	}
}