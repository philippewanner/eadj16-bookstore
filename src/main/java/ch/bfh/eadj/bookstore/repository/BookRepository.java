package ch.bfh.eadj.bookstore.repository;

import ch.bfh.eadj.bookstore.entity.Book;
import ch.bfh.eadj.bookstore.entity.User;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

/**
 * Jan
 */
public class BookRepository extends AbstractRepository<Book, String> {

    public BookRepository(EntityManager em) {
        super(em);
    }
    //search(String) List<BookInfo>

    public Book findByISBN(String isbn) {
        Map<String, Object> parameters = new HashMap<>(1);
        parameters.put("isbn", isbn.replace(" ", ""));

        List<Book> books = findByNamedQuery("Book.findByISBN", parameters);

        if (books.size() == 0) {
            return null;
        } else {
            return books.get(0);
        }
    }
    
    /*public Book findByKeyword(String[] keywords) {
        Map<String, Object> parameters = new HashMap<>(1);
        parameters.put("isbn", isbn.replace(" ", ""));

        List<Book> books = findByNamedQuery("Book.findByISBN", parameters);

        if (books.size() == 0) {
            return null;
        } else {
            return books.get(0);
        }
    }*/
}
