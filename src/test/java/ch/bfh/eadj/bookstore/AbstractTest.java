package ch.bfh.eadj.bookstore;

import ch.bfh.eadj.bookstore.entity.*;
import org.jboss.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.math.BigDecimal;
import java.util.List;

public abstract class AbstractTest {

    protected final static Logger LOGGER = Logger.getLogger(AbstractTest.class.getName());

    protected static EntityManagerFactory emf;
    protected static EntityManager em;

    private Long groupId;
    private Long userId;
    private Long customerId;

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

            Group group = new Group();
            group.setName("customer");
            em.persist(group);

            User user = new User();
            user.setName("bookstore");
            user.setPassword("bookstore");
            user.addGroup(group);

            em.persist(user);

            Customer customer = new Customer();
            customer.setName("Muster");
            customer.setFirstName("Hans");
            customer.setEmail("hans@muster.ch");
            customer.setNumber(123456L);
            customer.setUser(user);

            Address address = new Address();
            address.setStreet("Musterstrasse 55");
            address.setCity("Mürren");
            address.setPostalCode("3825");
            address.setState("BE");
            address.setCountry("Schweiz");
            customer.setAddress(address);

            CreditCard creditCard = new CreditCard();
            creditCard.setType(CreditCard.CreditCardType.MASTER_CARD);
            creditCard.setExpirationMonth(12);
            creditCard.setExpirationYear(2017);
            creditCard.setNumber("5555 5555 5555 5555");
            customer.setCreditCard(creditCard);

            em.persist(customer);            

            em.getTransaction().commit();

            fillBooks();            
            
            groupId = group.getId();
            userId = user.getId();
            customerId = customer.getId();

            em.clear();
            emf.getCache().evictAll();

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

            Group group = em.find(Group.class, groupId);
            em.remove(group);

            Customer customer = em.find(Customer.class, customerId);
            em.remove(customer);

            // TODO: remove Books
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

        List<Book> books = TestDataProvider.getBooks();
        for (int i = 0; i < books.size(); i++) {
            Book book = books.get(i);
            em.persist(book);
            //Long id = book.getId();
        }

        em.getTransaction().commit();
    }
}
