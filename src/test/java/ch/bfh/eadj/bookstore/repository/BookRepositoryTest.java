package ch.bfh.eadj.bookstore.repository;

import ch.bfh.eadj.bookstore.entity.Book;
import ch.bfh.eadj.bookstore.entity.User;
import ch.bfh.eadj.bookstore.repository.BookRepository;
import org.junit.Assert;
import org.junit.Before;

import ch.bfh.eadj.bookstore.AbstractTest;

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
	public void persistBook() {
		Book book=new Book();
                book.setTitle("Die Assistentinnen");
                book.setIsbn("978-3-86396-095-7");
                
                bookRepository.persist(book);
                            
		Assert.assertTrue(em.contains(book));
	}
        
        @Test
	public void findBook() {
		Book book = bookRepository.findByISBN("978-3-455-00000-7");
		Assert.assertNull(book);
	}
        
        @Test
	public void findUpdate() {
		Book book = bookRepository.findByISBN("978-3-455-00000-7");
		Assert.assertNull(book);
	}
}