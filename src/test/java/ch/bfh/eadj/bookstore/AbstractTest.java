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
            
            // TODO: remove books
            
        } catch (Exception e) {
            e.printStackTrace();
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        }
    }

    private void fillBooks() {
        em.getTransaction().begin();

        List<Book> books = TestDataProvider.getBooks();
        for (int i = 0; i < books.size(); i++) {
            Book book = books.get(i);
            em.persist(book);
            //Long id = book.getId();
        }

        em.getTransaction().commit();
    }
}
