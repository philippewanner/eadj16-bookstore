/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.application.service;

import java.util.List;
import java.util.logging.Level;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.books.application.exception.BookAlreadyExistsException;
import org.books.application.exception.BookNotFoundException;
import org.books.application.exception.CustomerAlreadyExistsException;
import org.books.persistence.TestDataProvider;
import org.books.persistence.dto.BookInfo;
import org.books.persistence.entity.Book;
import org.books.persistence.repository.BookRepository;
import org.junit.Before;
import org.jboss.logging.Logger;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 *
 * @author jlue4
 */
public class CatalogServiceBeanIT {

    private static final String CATALOG_SERVICE_NAME = "java:global/bookstore/CatalogService";

    private final static Logger LOGGER = Logger.getLogger(CatalogServiceBeanIT.class.getName());

    private CatalogService service;

    private String foundISBN;

    @BeforeClass
    public void setup() throws NamingException {
        service = (CatalogService) new InitialContext().lookup(CATALOG_SERVICE_NAME);
        assertNotNull(service);

        deleteBooks();
    }

    @Test
    public void addBooks() {
        LOGGER.info(">>>>>>>>>>>>>>>>>>> Catalog addBooks <<<<<<<<<<<<<<<<<<<<");

        List<Book> books = TestDataProvider.getBooks();
        for (int i = 0; i < books.size(); i++) {
            Book b = books.get(i);
            try {
                service.addBook(b);
            } catch (BookAlreadyExistsException ex) {
                LOGGER.warn("book not added " + b.getTitle());
            }
        }
    }

    @Test(dependsOnMethods = "addBooks")
    public void searchBook() {
        LOGGER.info(">>>>>>>>>>>>>>>>>>> Catalog searchBook <<<<<<<<<<<<<<<<<<<<");

        List<BookInfo> books = service.searchBooks("Nachtigall Hannah");

        assertNotNull(books);
        assertEquals(books.size(), 1);
        assertEquals(books.get(0).getIsbn(), "978-3-352-00885-6");

        foundISBN = books.get(0).getIsbn();
    }

    @Test(dependsOnMethods = "searchBook")
    public void searchISBN() throws BookNotFoundException {
        LOGGER.info(">>>>>>>>>>>>>>>>>>> Catalog searchISBN <<<<<<<<<<<<<<<<<<<<");

        Book book = service.findBook(foundISBN);

        assertNotNull(book);
        assertEquals(book.getIsbn(), "978-3-352-00885-6");
    }

    @Test(dependsOnMethods = "searchISBN", expectedExceptions = BookNotFoundException.class)
    public void searchBookNotFound() throws BookNotFoundException {
        LOGGER.info(">>>>>>>>>>>>>>>>>>> Catalog searchBook <<<<<<<<<<<<<<<<<<<<");

        service.findBook("000-0-000-00000-0");
    }

    private void deleteBooks() {
        // TODO
    }

}