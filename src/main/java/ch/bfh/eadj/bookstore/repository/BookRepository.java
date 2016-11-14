package ch.bfh.eadj.bookstore.repository;

import ch.bfh.eadj.bookstore.entity.Book;
import ch.bfh.eadj.bookstore.entity.User;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

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
    
    public List<Book> findByKeywords(String[] keywords) {
       
        Query q = em.createNativeQuery("SELECT * FROM Book WHERE (title LIKE ?1) or (authors LIKE ?1) or (publisher LIKE ?1)", Book.class);
        q.setParameter(1, "%"+keywords[0]+"%");
        List<Book> books = q.getResultList();        
        
        for (int b =books.size()-1;b>-1;b--){
            boolean containsKeyword=true;
            Book book=books.get(b);
            for (int i=1;i<keywords.length;i++){
                String keyword=keywords[i];
                if (!(book.getTitle().contains(keyword) || 
                        book.getAuthors().contains(keyword) || book.getPublisher().contains(keyword))){
                    containsKeyword=false;
                }
            }
            
            if (!containsKeyword){
                books.remove(book);
            }
        }

        return books;
    }
}
