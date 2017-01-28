/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.application.service;

import org.books.application.exception.BookAlreadyExistsException;
import org.books.application.exception.BookNotFoundException;
import org.books.persistence.dto.BookInfo;
import org.books.persistence.entity.Book;
import org.books.persistence.repository.BookRepository;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.util.List;

/**
 *
 * @author jan
 */
@Stateless(name = "CatalogService")
public class CatalogServiceBean extends AbstractService implements CatalogService {

    @EJB
    private BookRepository bookRepository;

    @EJB
    private AmazonCatalogBean amazonCatalog;

    @Override
    public Book findBook(String isbn) throws BookNotFoundException {
        logInfo("Book findBook(String isbn)");

        Book book = amazonCatalog.findBook(isbn);

        if (book == null) {
            logInfo("book not found " + isbn);
        } else {
            return book;
        }

        /*
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
         */
        throw new BookNotFoundException();
    }

    @Override
    public List<BookInfo> searchBooks(String keywords) {
        logInfo("List<BookInfo> searchBooks(String keywords)");

        List<BookInfo> books = amazonCatalog.searchBooks(keywords);
        return books;

        /*
        String[] temp = ConvertToArray(keywords);

        List<BookInfo> books = bookRepository.findByKeywords(temp);

        return books;
         */
    }

    @Override
    public void updateBook(Book book) throws BookNotFoundException {
        logInfo("updateBook(Book book)");

        List<Book> books = bookRepository.findByISBN(book.getIsbn());
        if (book == null || books.size() != 1) {
            throw new BookNotFoundException();
        }

        Book dbBook = bookRepository.find(books.get(0).getId());
                        
        book.setId(dbBook.getId());
        
        bookRepository.update(book);
    }

    @Override
    public void addBook(Book book) throws BookAlreadyExistsException {
        List<Book> b = bookRepository.findByISBN(book.getIsbn());

        if (b != null && b.size() > 0) {
            throw new BookAlreadyExistsException();
        }

        bookRepository.persist(book);
    }

    private String[] ConvertToArray(String keywords) {
        String[] array = keywords.split(" ");

        return array;
    }

}
