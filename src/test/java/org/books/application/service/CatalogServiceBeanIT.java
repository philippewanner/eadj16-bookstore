/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.application.service;

import org.books.DbUtil;
import org.books.application.exception.BookAlreadyExistsException;
import org.books.application.exception.BookNotFoundException;
import org.books.persistence.TestDataProvider;
import org.books.persistence.dto.BookInfo;
import org.books.persistence.entity.Book;
import org.jboss.logging.Logger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import static org.testng.Assert.*;

/**
 *
 * @author jlue4
 */
public class CatalogServiceBeanIT {

    private static final String CATALOG_SERVICE_NAME = "java:global/bookstore/CatalogService";

    private final static Logger LOGGER = Logger.getLogger(CatalogServiceBeanIT.class.getName());

    private final static BigDecimal NEW_PRICE = BigDecimal.valueOf(1000);

    private CatalogService service;

    private String foundISBN;
    private Book foundBook;

    @BeforeClass
    public void setup() throws NamingException, SQLException {
        LOGGER.info(">>>>>>>>>>>>>>>>>>> Catalog setup <<<<<<<<<<<<<<<<<<<<");

        service = (CatalogService) new InitialContext().lookup(CATALOG_SERVICE_NAME);
        assertNotNull(service);

        deleteBooks();
    }

    @AfterClass
    public void tearDown() throws SQLException {
        LOGGER.info(">>>>>>>>>>>>>>>>>>> Catalog tearDown <<<<<<<<<<<<<<<<<<<<");
        deleteBooks();
    }

    @Test
    public void addBooks() {
        LOGGER.info(">>>>>>>>>>>>>>>>>>> Catalog addBooks <<<<<<<<<<<<<<<<<<<<");

        List<Book> books = TestDataProvider.getBooks();
        for (Book b : books) {
            try {
                service.addBook(b);
            } catch (BookAlreadyExistsException ex) {
                LOGGER.warn("book not added " + b.getTitle());
            }
        }
    }

    @Test(dependsOnMethods = "addBooks", expectedExceptions = BookAlreadyExistsException.class)
    public void addBooksExists() throws BookAlreadyExistsException {
        LOGGER.info(">>>>>>>>>>>>>>>>>>> Catalog addBooksExists <<<<<<<<<<<<<<<<<<<<");

        List<Book> books = TestDataProvider.getBooks();

        Book b = books.get(0);

        service.addBook(b);
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

    @Test(dependsOnMethods = "addBooks")
    public void searchBookNothingFound() {
        LOGGER.info(">>>>>>>>>>>>>>>>>>> Catalog searchBookNothingFound <<<<<<<<<<<<<<<<<<<<");

        List<BookInfo> books = service.searchBooks("unbekanntes Buch King");

        assertNotNull(books);
        assertEquals(books.size(), 0);
    }

    @Test(dependsOnMethods = "searchBook")
    public void searchISBN() throws BookNotFoundException {
        LOGGER.info(">>>>>>>>>>>>>>>>>>> Catalog searchISBN <<<<<<<<<<<<<<<<<<<<");

        foundBook = service.findBook(foundISBN);

        assertNotNull(foundBook);
        assertEquals(foundBook.getIsbn(), "978-3-352-00885-6");
    }

    @Test(dependsOnMethods = "searchISBN")
    public void updateBook() throws BookNotFoundException {
        LOGGER.info(">>>>>>>>>>>>>>>>>>> Catalog updateBook <<<<<<<<<<<<<<<<<<<<");

        foundBook.setPrice(NEW_PRICE);

        service.updateBook(foundBook);
    }

    @Test(dependsOnMethods = "updateBook")
    public void searchUpdatedBook() throws BookNotFoundException {
        LOGGER.info(">>>>>>>>>>>>>>>>>>> Catalog searchUpdatedBook <<<<<<<<<<<<<<<<<<<<");

        Book b = service.findBook(foundISBN);

        assertNotNull(b);
        assertEquals(b.getPrice(), NEW_PRICE);
    }

    @Test(dependsOnMethods = "searchISBN", expectedExceptions = BookNotFoundException.class)
    public void searchBookNotFound() throws BookNotFoundException {
        LOGGER.info(">>>>>>>>>>>>>>>>>>> Catalog searchBook <<<<<<<<<<<<<<<<<<<<");

        service.findBook("000-0-000-00000-0");
    }

    private void deleteBooks() throws SQLException {
        LOGGER.info(">>>>>>>>>>>>>>>>>>> Catalog deleteBooks <<<<<<<<<<<<<<<<<<<<");

        DbUtil.executeSql("delete from BOOK");
    }
}