package ch.bfh.eadj.bookstore.repository;

import ch.bfh.eadj.bookstore.entity.Book;

import javax.persistence.EntityManager;

/**
 * Jan
 */
public class BookRepository extends AbstractRepository<Book, String> {

	public BookRepository(EntityManager em) {
		super(em);
	}
	//search(String) List<BookInfo>
}
