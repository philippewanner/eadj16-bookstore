/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.application.service;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
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
import org.junit.AfterClass;
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

    private final static BigDecimal NEW_PRICE = BigDecimal.valueOf(1000);

    private CatalogService service;

    private String foundISBN;
    private Book foundBook;

    @BeforeClass
    public void setup() throws NamingException {
        LOGGER.info(">>>>>>>>>>>>>>>>>>> Catalog setup <<<<<<<<<<<<<<<<<<<<");

        service = (CatalogService) new InitialContext().lookup(CATALOG_SERVICE_NAME);
        assertNotNull(service);

        deleteBooks();
    }

    @org.testng.annotations.AfterClass
    public void tearDown() {
        LOGGER.info(">>>>>>>>>>>>>>>>>>> Catalog tearDown <<<<<<<<<<<<<<<<<<<<");
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

    private void deleteBooks() {
        LOGGER.info(">>>>>>>>>>>>>>>>>>> Catalog deleteBooks <<<<<<<<<<<<<<<<<<<<");

        String JDBC_DRIVER = "org.apache.derby.jdbc.ClientDriver";
        String DB_URL = "jdbc:derby://localhost:1527/bookstore";
        String USER = "app";
        String PASS = "app";
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName(JDBC_DRIVER);

            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            //delete books            
            stmt = conn.createStatement();
            String sql;
            sql = "delete from BOOK";
            stmt.executeQuery(sql);
        } catch (Exception ex) {
        }
    }

}
