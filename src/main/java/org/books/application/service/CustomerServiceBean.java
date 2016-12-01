/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.application.service;

import java.util.List;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import org.books.application.dto.Registration;
import org.books.application.exception.BookNotFoundException;
import org.books.application.exception.CustomerAlreadyExistsException;
import org.books.application.exception.CustomerNotFoundException;
import org.books.application.exception.InvalidPasswordException;
import org.books.persistence.dto.BookInfo;
import org.books.persistence.dto.CustomerInfo;
import org.books.persistence.entity.Book;
import org.books.persistence.entity.Customer;

/**
 *
 * @author jan
 */
@Stateless(name="CustomerService")
public class CustomerServiceBean extends AbstractService implements CustomerService {

    public CustomerServiceBean(){
        this.logger = Logger.getLogger(CustomerServiceBean.class.getName());
    }

    @Override
    public void authenticateCustomer(String email, String password) throws CustomerNotFoundException, InvalidPasswordException {
        logInfo("authenticateCustomer");
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void changePassword(String email, String password) throws CustomerNotFoundException {
        logInfo("changePassword");
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Customer findCustomer(Long customerNr) throws CustomerNotFoundException {
        logInfo("findCustomer(Long customerNr)");
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Customer findCustomer(String email) throws CustomerNotFoundException {
        logInfo("findCustomer(String email)");
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Long registerCustomer(Registration registration) throws CustomerAlreadyExistsException {
        logInfo("registerCustomer");
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<CustomerInfo> searchCustomers(String name) {
        logInfo("searchCustomers");
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updateCustomer(Customer customer) throws CustomerNotFoundException, CustomerAlreadyExistsException {
        logInfo("updateCustomer");
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
   
    
}
