/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.application.service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.books.application.exception.BookNotFoundException;
import org.books.persistence.dto.BookInfo;
import org.books.persistence.entity.Book;
import org.jboss.logging.Logger;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 *
 * @author jl
 */
public class AmazonCatalogBeanIT {

    private final static Logger LOGGER = Logger.getLogger(CatalogServiceBeanIT.class.getName());

    @BeforeClass
    public void setup() throws NamingException, SQLException {
        LOGGER.info(">>>>>>>>>>>>>>>>>>> AmazonCatalogBeanIT <<<<<<<<<<<<<<<<<<<<");
    }

    @Test
    public void searchISBN() throws BookNotFoundException {
        LOGGER.info(">>>>>>>>>>>>>>>>>>> AmazonCatalogBeanIT searchISBN <<<<<<<<<<<<<<<<<<<<");

        AmazonCatalogBean a = new AmazonCatalogBean();
        Book b = a.findBook("1338099132");

        assertNotNull(b);
        assertEquals(b.getIsbn(), "1338099132");
        assertEquals(b.getTitle().substring(0, 16), "Harry Potter and");
        Integer y = 2016;
        assertEquals(b.getPublicationYear(), y);
        assertEquals(b.getPrice().doubleValue(), 29.99);
    }

    @Test(expectedExceptions = BookNotFoundException.class)
    public void searchWrongISBN() throws BookNotFoundException {
        LOGGER.info(">>>>>>>>>>>>>>>>>>> AmazonCatalogBeanIT searchWrongISBN <<<<<<<<<<<<<<<<<<<<");

        AmazonCatalogBean a = new AmazonCatalogBean();
        Book b = a.findBook("1234567890");
    }

    @Test
    public void searchKeywords() {
        LOGGER.info(">>>>>>>>>>>>>>>>>>> AmazonCatalogBeanIT searchKeywords <<<<<<<<<<<<<<<<<<<<");

        AmazonCatalogBean a = new AmazonCatalogBean();
        List<BookInfo> bis = a.searchBooks("Java EE");

        assertNotNull(bis);
        assertEquals(bis.size(), 79);
    }
}
