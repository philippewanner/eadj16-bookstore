package org.books.application.service;

import org.books.application.exception.BookNotFoundException;
import org.books.persistence.dto.BookInfo;
import org.books.persistence.entity.Book;

import java.util.List;

/*
Jan
 */
public interface CatalogService {

	Book findBook(long id) throws BookNotFoundException;

	Book findBook(String isbn) throws BookNotFoundException;

	List<BookInfo> searchBooks(String keywords);

	void updateBook(Book book) throws BookNotFoundException;
}