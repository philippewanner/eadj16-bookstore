/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.application.service;

import java.util.List;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import org.books.application.exception.BookNotFoundException;
import org.books.persistence.dto.BookInfo;
import org.books.persistence.entity.Book;

/**
 *
 * @author jan
 */
@Stateless(name="CatalogService")
public class CatalogServiceBean extends AbstractService implements CatalogService {

    public CatalogServiceBean(){
        this.logger = Logger.getLogger(CatalogServiceBean.class.getName());
    }
    
    @Override
    public Book findBook(long id) throws BookNotFoundException {
        logInfo("Book findBook(long id)");
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Book findBook(String isbn) throws BookNotFoundException {
        logInfo("Book findBook(String isbn)");
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<BookInfo> searchBooks(String keywords) {
        logInfo("List<BookInfo> searchBooks(String keywords)");
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updateBook(Book book) throws BookNotFoundException {
        logInfo("updateBook(Book book)");
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
