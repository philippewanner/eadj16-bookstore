package ch.bfh.eadj.bookstore;

import ch.bfh.eadj.bookstore.entity.Book;
import ch.bfh.eadj.bookstore.entity.User;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.jboss.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public abstract class AbstractTest {

    protected final static Logger LOGGER = Logger.getLogger(AbstractTest.class.getName());

    protected static EntityManagerFactory emf;
    protected static EntityManager em;

    private Long userId;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        emf = Persistence.createEntityManagerFactory("bookstore");
        em = emf.createEntityManager();
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        if (em != null) {
            em.close();
        }
        if (emf != null) {
            emf.close();
        }
    }

    @Before
    public void setUp() {
        try {
            em.getTransaction().begin();

            User user = new User();
            user.setName("bookstore");
            user.setPassword("bookstore");

            em.persist(user);

            em.getTransaction().commit();

            userId = user.getId();

            em.clear();
            emf.getCache().evictAll();

            fillBooks();

        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        }
    }

    @After
    public void tearDown() {
        try {
            em.getTransaction().begin();

            User user = em.find(User.class, userId);
            em.remove(user);

            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        }
    }

    private void fillBooks() {
        em.getTransaction().begin();

        for (int i = 0; i < 3; i++) {
            Book book = createBook(i);
            em.persist(book);
            //Long id = book.getId();
        }

        em.getTransaction().commit();
    }

    private Book createBook(int i) {
        Book book = new Book();

        switch (i) {
            case 0:
                book.setIsbn("978-3-455-65045-7");
                book.setAuthors("Antoine Laurain");
                book.setBinding(Book.BookBinding.HARD_COVER);
                book.setNumberOfPages(192);
                book.setPrice(BigDecimal.valueOf(28.90));
                book.setPublicationYear(2016);
                book.setPublisher("Atlantik Verlag");
                book.setTitle("Das Bild aus meinem Traum");
                break;

            case 1:
                book.setIsbn("978-3-352-00885-6");
                book.setAuthors("Kristin Hannah");
                book.setBinding(Book.BookBinding.HARD_COVER);
                book.setNumberOfPages(608);
                book.setPrice(BigDecimal.valueOf(25.90));
                book.setPublicationYear(2016);
                book.setPublisher("Ruetten & Loening");
                book.setTitle("Die Nachtigall");
                break;

            case 2:
                book.setIsbn("978-3-8105-2471-3");
                book.setAuthors("Lori Nelson Spielman");
                book.setBinding(Book.BookBinding.HARD_COVER);
                book.setNumberOfPages(384);
                book.setPrice(BigDecimal.valueOf(21.90));
                book.setPublicationYear(2016);
                book.setPublisher("Fischer Krüger");
                book.setTitle("Und nebenan warten die Sterne");
                break;
        }

        return book;
    }
}
