package org.books.application.service;

import org.books.application.dto.Registration;
import org.books.application.exception.CustomerAlreadyExistsException;
import org.books.application.exception.CustomerNotFoundException;
import org.books.application.exception.InvalidPasswordException;
import org.books.persistence.dto.CustomerInfo;
import org.books.persistence.entity.Customer;

import java.util.List;

public interface CustomerService {

	void authenticateCustomer(String email, String password) throws CustomerNotFoundException, InvalidPasswordException;

	void changePassword(String email, String password) throws CustomerNotFoundException;

	Customer findCustomer(Long customerNr) throws CustomerNotFoundException;

	Customer findCustomer(String email) throws CustomerNotFoundException;

	Long registerCustomer(Registration registration) throws CustomerAlreadyExistsException;

	List<CustomerInfo> searchCustomers(String name);

	void updateCustomer(Customer customer) throws CustomerNotFoundException, CustomerAlreadyExistsException;
}