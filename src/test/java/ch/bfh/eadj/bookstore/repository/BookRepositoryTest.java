package ch.bfh.eadj.bookstore.repository;

import ch.bfh.eadj.bookstore.entity.Book;
import ch.bfh.eadj.bookstore.entity.User;
import ch.bfh.eadj.bookstore.repository.BookRepository;
import org.junit.Assert.*;
import org.junit.Before;

import ch.bfh.eadj.bookstore.AbstractTest;
import ch.bfh.eadj.bookstore.TestDataProvider;
import java.util.List;
import org.junit.Assert;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class BookRepositoryTest extends AbstractTest {

	private BookRepository bookRepository;

	@Before
	public void setUpBeforeTest() {
		bookRepository = new BookRepository(em);
	}

	@Test
	public void searchByISBN() {
            
            
		Book book = bookRepository.findByISBN("978-3-455-65045-7");
		Assert.assertNotNull(book);
		Assert.assertEquals("978-3-455-65045-7", book.getIsbn());
                Assert.assertEquals("Das Bild aus meinem Traum", book.getTitle());
                
                book = bookRepository.findByISBN(" 978-3-8105- 2471 -3");
		Assert.assertNotNull(book);
		Assert.assertEquals("978-3-8105-2471-3", book.getIsbn());
                Assert.assertEquals("Und nebenan warten die Sterne", book.getTitle());
                
            
	}
        
        @Test
	public void notFoundByISBN() {
		Book book = bookRepository.findByISBN("978-3-455-00000-7");
		Assert.assertNull(book);
	}
        
        @Test
	public void insertDelete() {
		Book book=new Book();
                book.setTitle("Die Assistentinnen");
                book.setIsbn("978-3-86396-095-7");
                
                em.getTransaction().begin();
                
                bookRepository.persist(book);
                
                em.getTransaction().commit();
                            
		Assert.assertNotNull(book.getId());
                
                String id = book.getId();                
                               
                em.getTransaction().begin();
		book = bookRepository.find(id);
		bookRepository.delete(book);
		em.getTransaction().commit();

		book = bookRepository.find(id);

                Assert.assertNull(book);
	}
        
        @Test
	public void findBook() {
            Book book = bookRepository.find(TestDataProvider.getISBNs().get(0));
            Assert.assertNotNull(book);
	}
        
        @Test
	public void findByKeywords() {
            String[] keywords = {"Nachtigall", "Kristin"};            
            List<Book> book = bookRepository.findByKeywords(keywords);            
            Assert.assertNotNull(book);
            Assert.assertEquals(1, book.size());
            
            String[] keywords2 = {"Lori", "Fischer"};            
            book = bookRepository.findByKeywords(keywords2);            
            Assert.assertNotNull(book);
            Assert.assertEquals(1, book.size());
            
                        
            String[] keywords4 = {"Atlantik"};            
            book = bookRepository.findByKeywords(keywords4);            
            Assert.assertNotNull(book);
            Assert.assertEquals(2, book.size());
            
            String[] keywords5 = {"Atlantik", "Weihnachtspudding"};            
            book = bookRepository.findByKeywords(keywords5);            
            Assert.assertNotNull(book);
            Assert.assertEquals(1, book.size());
	}
        
        @Test
	public void findUpdate() {
		//Book book = bookRepository.findByISBN("978-3-455-00000-7");
		//Assert.assertNull(book);
	}
}