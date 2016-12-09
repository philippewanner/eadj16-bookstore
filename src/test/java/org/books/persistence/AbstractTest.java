package org.books.persistence;

import org.books.persistence.entity.*;
import org.books.persistence.enumeration.CreditCardType;
import org.books.persistence.enumeration.OrderStatus;
import org.books.persistence.enumeration.UserGroup;
import org.jboss.logging.Logger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import org.books.DbUtil;

public abstract class AbstractTest {

    protected final static Logger LOGGER = Logger.getLogger(AbstractTest.class.getName());

    protected static EntityManagerFactory emf;
    protected static EntityManager em;

    private Long userId;
    private Long customerId;

    private List<Long> bookIds;

    private Long salesOrderId;

    @BeforeClass
    public void setUpBeforeClass() throws Exception {
        LOGGER.info("--------------- setUpBeforeClass --------------------");

        DbUtil.executeSqlTestDB("DELETE FROM SALESORDER_SALESORDERITEM");
        DbUtil.executeSqlTestDB("DELETE FROM SALESORDERITEM");
        DbUtil.executeSqlTestDB("DELETE FROM SALESORDER");
        DbUtil.executeSqlTestDB("DELETE FROM USERLOGIN");
        DbUtil.executeSqlTestDB("DELETE FROM CUSTOMER");
        DbUtil.executeSqlTestDB("DELETE FROM BOOK");

        emf = Persistence.createEntityManagerFactory("bookstore-test");
        em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            Login login = new Login();
            login.setUserName("hans@muster.ch");
            login.setPassword("bookstore");
            login.setGroup(UserGroup.CUSTOMER);

            em.persist(login);

            Customer customer = new Customer();
            customer.setLastName("Muster");
            customer.setFirstName("Hans");
            customer.setEmail("hans@muster.ch");

            Address address = new Address();
            address.setStreet("Musterstrasse 55");
            address.setCity("Mürren");
            address.setPostalCode("3825");
            address.setState("BE");
            address.setCountry("Schweiz");
            customer.setAddress(address);

            CreditCard creditCard = new CreditCard();
            creditCard.setType(CreditCardType.MASTER_CARD);
            creditCard.setExpirationMonth(12);
            creditCard.setExpirationYear(2017);
            creditCard.setNumber("5555 5555 5555 5555");
            customer.setCreditCard(creditCard);

            em.persist(customer);

            em.getTransaction().commit();

            fillBooks();

            userId = login.getId();
            customerId = customer.getNumber();

            this.salesOrderId = this.getNewPersistedSalesOrder().getId();

            em.clear();
            emf.getCache().evictAll();

        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        }
    }

    @AfterClass
    public void tearDownAfterClass() throws Exception {

        /*  JL: excluded in order to skip local build errors
            
                LOGGER.info("--------------- tearDownAfterClass --------------------");
            
		try {
			em.getTransaction().begin();

			em.remove(em.find(SalesOrder.class, this.salesOrderId));

			Login login = em.find(Login.class, userId);
			em.remove(login);

			Customer customer = em.find(Customer.class, customerId);
			em.remove(customer);

			removeBooks();

			em.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
		}

		if (em != null) {
			em.close();
		}
		if (emf != null) {
			emf.close();
		}*/
    }

    protected Customer getPersistedCustomer() {

        return em.find(Customer.class, customerId);
    }

    protected SalesOrder getPersistedSalesOrder() {

        return em.find(SalesOrder.class, salesOrderId);
    }

    private void fillBooks() {
        bookIds = new ArrayList<>();

        List<Book> books = TestDataProvider.getBooks();
        for (Book book : books) {
            em.getTransaction().begin();
            em.persist(book);
            em.getTransaction().commit();
            Long id = book.getId();
            bookIds.add(id);
        }
    }

    private void removeBooks() {
        for (Long id : bookIds) {
            Book book = em.find(Book.class, id);
            em.remove(book);
        }
    }

    private SalesOrder getNewPersistedSalesOrder() {
        SalesOrder salesOrder = new SalesOrder();
        salesOrder.setNumber(2L);
        salesOrder.setAddress(new Address("street", "city", "postalCode", "state", "country"));
        salesOrder.setAmount(new BigDecimal(10.5));
        salesOrder.setCreditCard(new CreditCard());
        salesOrder.setCustomer(getPersistedCustomer());
        SimpleDateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy");

        Date orderDate = null;
        try {
            orderDate = dateformat.parse("17/07/1989");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        salesOrder.setDate(orderDate);
        salesOrder.setStatus(OrderStatus.ACCEPTED);
        salesOrder.setSalesOrderItems(getNewPersistedSalesOrderItems());

        em.getTransaction().begin();
        em.persist(salesOrder);
        em.getTransaction().commit();

        return salesOrder;
    }

    private Set<SalesOrderItem> getNewPersistedSalesOrderItems() {
        SalesOrderItem salesOrderItem = new SalesOrderItem();
        salesOrderItem.setBook(em.find(Book.class, bookIds.get(0)));
        salesOrderItem.setPrice(new BigDecimal(1));
        salesOrderItem.setQuantity(12);

        em.getTransaction().begin();
        em.persist(salesOrderItem);
        em.getTransaction().commit();

        Set<SalesOrderItem> salesOrderItems = new HashSet<>();
        salesOrderItems.add(salesOrderItem);

        return salesOrderItems;
    }
}
