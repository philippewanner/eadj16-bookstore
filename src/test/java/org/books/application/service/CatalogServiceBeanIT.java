/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.application.service;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.books.persistence.repository.BookRepository;
import org.junit.Before;

/**
 *
 * @author jlue4
 */
public class CatalogServiceBeanIT {

    //public static final String CATALOG_SERVICE_NAME = "java:global/Bank1/AccountService!org.example.bank1.AccountService";

    private CatalogService catalogService;

    @Before
    public void setUpBeforeTest() throws NamingException {
      //  catalogService = (CatalogService) new InitialContext().lookup(CATALOG_SERVICE_NAME);

    }
    
    
}
