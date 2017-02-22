/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.application.rest;

import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.books.application.exception.BookAlreadyExistsException;
import org.books.application.exception.BookNotFoundException;
import org.books.application.service.CatalogService;
import org.books.persistence.dto.BookInfo;
import org.books.persistence.entity.Book;
import org.books.persistence.enumeration.BookBinding;
import org.books.persistence.repository.BookRepository;

/**
 *
 * @author jl
 */
// Glassfish 4.1.0 workaround
@Path("books")
public class CatalogResource {

    protected static Logger logger;

    @EJB
    private CatalogService service;

    public CatalogResource() {
        logger = Logger.getLogger(getClass().getName());
    }

    @GET
    @Path("{isbn}")
    @Produces({APPLICATION_JSON, APPLICATION_XML})
    public Book findBook(@PathParam("isbn") String isbn) {
        try {
            this.logInfo("findBook " + isbn);
            Book b = service.findBook(isbn);
            return b;
        } catch (BookNotFoundException ex) {
            this.logWarn("book not found " + isbn);
            throw new WebApplicationException("Book not found", Response.Status.NOT_FOUND);
        }
    }

    @GET
    @Produces({APPLICATION_JSON, APPLICATION_XML})
    public List<BookInfo> searchBooks(@QueryParam("keywords") String keywords) {

        this.logInfo("searchBooks " + keywords);

        if (keywords == null || keywords.length() == 0) {
            this.logWarn("keywords missing");
            throw new WebApplicationException("Keywords missing", Response.Status.BAD_REQUEST);
        }

        List<BookInfo> books = service.searchBooks(keywords);
        return books;
    }

    @PUT
    @Path("{isbn}")
    @Consumes({APPLICATION_JSON, APPLICATION_XML})
    public void updateBook(@PathParam("isbn") String isbn, Book book) {

        this.logInfo("updateBook " + book.getIsbn());

        if (book.getIsbn() == null || book.getIsbn().length() == 0) {
            book.setIsbn(isbn);
        }

        if (!isBookDataComplete(book)) {
            this.logWarn("book incomplete");
            throw new WebApplicationException("Book data incomplete", Response.Status.BAD_REQUEST);
        }

        if (!isbn.equals(book.getIsbn())) {
            this.logWarn("ISBN does not match");
            throw new WebApplicationException("ISBN does not match", Response.Status.BAD_REQUEST);
        }

        try {
            service.updateBook(book);
        } catch (BookNotFoundException ex) {
            this.logWarn("Book not found");
            throw new WebApplicationException("Book not found", Response.Status.NOT_FOUND);
        }
    }

    @POST
    @Consumes({APPLICATION_JSON, APPLICATION_XML})
    @Produces(TEXT_PLAIN)
    public Response addBook(Book book) {

        this.logInfo("addBook " + book.getIsbn());

        try {
            service.addBook(book);
        } catch (BookAlreadyExistsException ex) {
            this.logWarn("Book already exists " + book.getIsbn());
            throw new WebApplicationException("Book already exists", Response.Status.CONFLICT);
        }

        return Response.status(Response.Status.CREATED).build();
    }

    private void logInfo(String msg) {
        logger.log(Level.INFO, "CatalogResource: " + msg);
    }

    private void logWarn(String msg) {
        logger.log(Level.WARNING, "CatalogResource: " + msg);
    }

    private boolean isBookDataComplete(Book book) {
        // debugging style
        if (book.getAuthors() == null || book.getAuthors().length() == 0
                || book.getBinding() == BookBinding.UNKNOWN
                || book.getIsbn() == null || book.getIsbn().length() == 0
                || book.getNumberOfPages() == 0
                || book.getPrice() == null || book.getPrice() == BigDecimal.ZERO
                || book.getPublicationYear() == 0
                || book.getPublisher() == null || book.getPublisher().length() == 0
                || book.getTitle() == null || book.getTitle().length() == 0) {
            return false;
        }
        return true;
    }
}
