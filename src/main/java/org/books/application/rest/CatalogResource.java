/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.application.rest;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.books.application.exception.BookNotFoundException;
import org.books.application.service.CatalogService;
import org.books.persistence.dto.BookInfo;
import org.books.persistence.entity.Book;

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
            throw new WebApplicationException("book not found", Response.Status.NOT_FOUND);
        }
    }

    @GET
    @Produces({APPLICATION_JSON, APPLICATION_XML})
    public List<BookInfo> searchBooks(@QueryParam("keywords") String keywords) {
        
        this.logInfo("searchBooks " + keywords);
        
        if (keywords == null || keywords.length() == 0) {
            this.logWarn("keywords missing");
            throw new WebApplicationException("keywords missing", Response.Status.BAD_REQUEST);
        }
        
        List<BookInfo> books = service.searchBooks(keywords);
        return books;
    }

    //public void updateBook(Book book){}
    //public void addBook(Book book) {}
    private void logInfo(String msg) {
        logger.log(Level.INFO, "CatalogResource: " + msg);
    }

    private void logWarn(String msg) {
        logger.log(Level.WARNING, "CatalogResource: " + msg);
    }
}
