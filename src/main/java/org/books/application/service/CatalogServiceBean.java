/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.application.service;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import org.books.application.exception.BookAlreadyExistsException;
import org.books.application.exception.BookNotFoundException;
import org.books.persistence.dto.BookInfo;
import org.books.persistence.entity.Book;
import org.books.persistence.repository.BookRepository;

/**
 *
 * @author jan
 */
@Stateless(name = "CatalogService")
public class CatalogServiceBean extends AbstractService implements CatalogService {

    @EJB
    private BookRepository bookRepository;

    @Override
    public Book findBook(String isbn) throws BookNotFoundException {
        logInfo("Book findBook(String isbn)");

        List<Book> b = bookRepository.findByISBN(isbn);

        if (b == null) {
            logInfo("findBook: empty list");
        } else if (b.size() == 0) {
            logInfo("no book found");

        } else if (b.size() > 1) {
            logInfo("multiple books ot found");
        } else {
            return b.get(0);
        }

        throw new BookNotFoundException();
    }

    @Override
    public List<BookInfo> searchBooks(String keywords) {
        logInfo("List<BookInfo> searchBooks(String keywords)");

        String[] temp = ConvertToArray(keywords);

        List<BookInfo> books = bookRepository.findByKeywords(temp);

        return books;
    }

    @Override
    public void updateBook(Book book) throws BookNotFoundException {
        logInfo("updateBook(Book book)");

        List<Book> books = bookRepository.findByISBN(book.getIsbn());
        if (book == null || books.size() != 1) {
            throw new BookNotFoundException();
        }

        bookRepository.update(book);
    }

    @Override
    public void addBook(Book book) throws BookAlreadyExistsException{
        bookRepository.persist(book);
    }
    
    private String[] ConvertToArray(String keywords) {
        String[] array = keywords.split(" ");

        return array;
    }

}
