package org.books.application.service;

import org.books.application.exception.BookAlreadyExistsException;
import org.books.application.exception.BookNotFoundException;
import org.books.persistence.dto.BookInfo;
import org.books.persistence.entity.Book;

import javax.ejb.Remote;
import java.util.List;

/*
Jan
 */
@Remote
public interface CatalogService {

    void addBook(Book book) throws BookAlreadyExistsException;
    
    Book findBook(String isbn) throws BookNotFoundException;

    List<BookInfo> searchBooks(String keywords);

    void updateBook(Book book) throws BookNotFoundException;
}
