package org.books.persistence.repository;

import org.books.persistence.AbstractTest;
import org.books.persistence.TestDataProvider;
import org.books.persistence.dto.BookInfo;
import org.books.persistence.entity.Book;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.*;

public class BookRepositoryTest extends AbstractTest {

	private BookRepository bookRepository;

	@BeforeClass
	public void setUpBeforeTest() {
		bookRepository = new BookRepository();
		bookRepository.setEntityManager(em);
	}

	@Test
	public void searchByISBN() {

		LOGGER.info(">>>>>>>>>>>>>>>>>>> Book searchByISBN <<<<<<<<<<<<<<<<<<<<");

		List<Book> books = bookRepository.findByISBN("978-3-455-65045-7");
		assertNotNull(books);
		assertEquals(1, books.size());
		assertEquals("978-3-455-65045-7", books.get(0).getIsbn());
		assertEquals("Das Bild aus meinem Traum", books.get(0).getTitle());

		books = bookRepository.findByISBN(" 978-3-8105- 2471 -3");
		assertNotNull(books);
		assertEquals(1, books.size());
		assertEquals("978-3-8105-2471-3", books.get(0).getIsbn());
		assertEquals("Und nebenan warten die Sterne", books.get(0).getTitle());
	}

	@Test
	public void notFoundByISBN() {

		LOGGER.info(">>>>>>>>>>>>>>>>>>> Book isbn not found <<<<<<<<<<<<<<<<<<<<");

		List<Book> book = bookRepository.findByISBN("978-3-455-00000-7");
		assertEquals(0, book.size());
	}

	@Test
	public void insertDelete() {

		LOGGER.info(">>>>>>>>>>>>>>>>>>> Book insertDelete <<<<<<<<<<<<<<<<<<<<");

		Book book = new Book();
		book.setTitle("Die Assistentinnen");
		book.setIsbn("978-3-86396-095-7");

		em.getTransaction().begin();

		bookRepository.persist(book);

		em.getTransaction().commit();

		assertNotNull(book.getId());

		Long id = book.getId();

		em.getTransaction().begin();

		book = bookRepository.find(id);

		assertNotNull(book);

		bookRepository.delete(book);
		em.getTransaction().commit();

		book = bookRepository.find(id);

		assertNull(book);
	}

	@Test
	public void findBook() {

		LOGGER.info(">>>>>>>>>>>>>>>>>>> Book findBook <<<<<<<<<<<<<<<<<<<<");

		for (String isbn : TestDataProvider.getISBNs()) {
			List<Book> books = bookRepository.findByISBN(isbn);
			assertNotNull(books);
			assertEquals(1, books.size());
		}
	}

	@Test
	public void findPublicationYearInfos() {

		LOGGER.info(">>>>>>>>>>>>>>>>>>> Book find by publication year <<<<<<<<<<<<<<<<<<<<");

		List<BookInfo> infos = bookRepository.findInfosByPublicationYear(2016);

		assertNotNull(infos);
		assertEquals(6, infos.size());
	}

	@Test
	public void findNoPublicationYearInfos() {

		LOGGER.info(">>>>>>>>>>>>>>>>>>> Book find nothing by publication year <<<<<<<<<<<<<<<<<<<<");

		List<BookInfo> infos = bookRepository.findInfosByPublicationYear(1999);

		assertNotNull(infos);
		assertEquals(0, infos.size());
	}

	@Test
	public void getAllInfos() {

		LOGGER.info(">>>>>>>>>>>>>>>>>>> Book get all <<<<<<<<<<<<<<<<<<<<");

		List<BookInfo> infos = bookRepository.getInfosAll();

		assertEquals(7, infos.size());
	}

	@Test
	public void findByKeywords() {

		LOGGER.info(">>>>>>>>>>>>>>>>>>> Book findByKeywords <<<<<<<<<<<<<<<<<<<<");

		String[] keywords = { "Nachtigall", "Kristin" };
		List<BookInfo> book = bookRepository.findByKeywords(keywords);
		assertNotNull(book);
		assertEquals(1, book.size());
		assertEquals("978-3-352-00885-6", book.get(0).getIsbn());

		String[] keywords2 = { "LORI", "Fischer" };
		book = bookRepository.findByKeywords(keywords2);
		assertNotNull(book);
		assertEquals(1, book.size());

		String[] keywords4 = { "Atlantik" };
		book = bookRepository.findByKeywords(keywords4);
		assertNotNull(book);
		assertEquals(2, book.size());

		String[] keywords5 = { "atlantik", "weihnachtspudding" };
		book = bookRepository.findByKeywords(keywords5);
		assertNotNull(book);
		assertEquals(1, book.size());

		String[] keywords6 = { "Laurain", "Traum", "Atlantik" };
		book = bookRepository.findByKeywords(keywords6);
		assertNotNull(book);
		assertEquals(1, book.size());

		String[] keywords7 = { "Laurain", "irgendwas", "Atlantik" };
		book = bookRepository.findByKeywords(keywords7);
		assertNotNull(book);
		assertEquals(0, book.size());
	}

	@Test(dependsOnMethods = "searchByISBN")
	public void findUpdate() {

		LOGGER.info(">>>>>>>>>>>>>>>>>>> Book find update <<<<<<<<<<<<<<<<<<<<");

		String testIsbn = TestDataProvider.getISBNs().get(0);
		List<Book> books = bookRepository.findByISBN(testIsbn);
		assertNotNull(books);
		assertEquals(1, books.size());
		Book book = books.get(0);

		String testTitle = "MyNew Title";

		book.setTitle(testTitle);

		em.getTransaction().begin();

		bookRepository.update(book);

		em.getTransaction().commit();
		em.clear();
		emf.getCache().evictAll();

		books = bookRepository.findByISBN(testIsbn);
		assertEquals(1, books.size());
		book = books.get(0);

		assertEquals(testTitle, book.getTitle());
	}
}
