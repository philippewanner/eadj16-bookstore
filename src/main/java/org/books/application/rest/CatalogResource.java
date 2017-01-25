/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.application.rest;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
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
import org.books.persistence.entity.Book;

/**
 *
 * @author jl
 */
@Stateless
@Path("books")
public class CatalogResource {

    protected static Logger logger;
    
    @Context
    Request request;
    @Context
    UriInfo uriInfo;
    @Context
    HttpHeaders headers;


    @EJB
    private CatalogService service;
    
    public CatalogResource(){
        logger = Logger.getLogger(getClass().getName());
    }

    //public void addBook(Book book) {}
    
    @GET
    @Path("{isbn}")
    @Produces({APPLICATION_JSON, APPLICATION_XML})
    public Book findBook(@PathParam("isbn") String isbn) {
        try {
            Book b = service.findBook(isbn);
            return b;
        } catch (BookNotFoundException ex) {
            logger.log(Level.SEVERE, isbn);
            throw new WebApplicationException("book not found", Response.Status.NOT_FOUND);
        }
    }

    //public List<BookInfo> searchBooks(String keywords){}
    //public void updateBook(Book book){}
}
