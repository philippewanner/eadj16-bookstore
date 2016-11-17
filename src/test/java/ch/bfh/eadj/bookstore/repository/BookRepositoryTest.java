package ch.bfh.eadj.bookstore.repository;

import ch.bfh.eadj.bookstore.AbstractTest;
import ch.bfh.eadj.bookstore.TestDataProvider;
import ch.bfh.eadj.bookstore.dto.BookInfo;
import ch.bfh.eadj.bookstore.entity.Book;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

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
        Book book = new Book();
        book.setTitle("Die Assistentinnen");
        book.setIsbn("978-3-86396-095-7");

        em.getTransaction().begin();

        bookRepository.persist(book);

        em.getTransaction().commit();

        Assert.assertNotNull(book.getId());

        Long id = book.getId();

        em.getTransaction().begin();

        book = bookRepository.find(id);

        Assert.assertNotNull(book);

        bookRepository.delete(book);
        em.getTransaction().commit();

        book = bookRepository.find(id);

        Assert.assertNull(book);
    }

    @Test
    public void findBook() {
        for (String isbn : TestDataProvider.getISBNs()) {
            Book book = bookRepository.findByISBN(isbn);
            Assert.assertNotNull(book);
        }
    }

    @Test
    public void findByKeywords() {
        String[] keywords = {"Nachtigall", "Kristin"};
        List<BookInfo> book = bookRepository.findByKeywords(keywords);
        Assert.assertNotNull(book);
        Assert.assertEquals(1, book.size());
        Assert.assertEquals("978-3-352-00885-6", book.get(0).getIsbn());

        String[] keywords2 = {"LORI", "Fischer"};
        book = bookRepository.findByKeywords(keywords2);
        Assert.assertNotNull(book);
        Assert.assertEquals(1, book.size());

        String[] keywords4 = {"Atlantik"};
        book = bookRepository.findByKeywords(keywords4);
        Assert.assertNotNull(book);
        Assert.assertEquals(2, book.size());

        String[] keywords5 = {"atlantik", "weihnachtspudding"};
        book = bookRepository.findByKeywords(keywords5);
        Assert.assertNotNull(book);
        Assert.assertEquals(1, book.size());

        String[] keywords6 = {"Laurain", "Traum", "Atlantik"};
        book = bookRepository.findByKeywords(keywords6);
        Assert.assertNotNull(book);
        Assert.assertEquals(1, book.size());

        String[] keywords7 = {"Laurain", "irgendwas", "Atlantik"};
        book = bookRepository.findByKeywords(keywords7);
        Assert.assertNotNull(book);
        Assert.assertEquals(0, book.size());
    }

    @Test
    public void findUpdate() {
        String testIsbn = TestDataProvider.getISBNs().get(0);
        Book book = bookRepository.findByISBN(testIsbn);
        Assert.assertNotNull(book);

        String testTitle = "MyNew Title";

        book.setTitle(testTitle);

        em.getTransaction().begin();

        bookRepository.update(book);

        em.getTransaction().commit();
        em.clear();
        emf.getCache().evictAll();

        book = bookRepository.findByISBN(testIsbn);
        Assert.assertNotNull(book);

        Assert.assertEquals(testTitle, book.getTitle());
    }
}
