package org.books.persistence;

import org.books.persistence.entity.*;
import org.books.persistence.enumeration.CreditCardType;
import org.books.persistence.enumeration.OrderStatus;
import org.jboss.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public abstract class AbstractTest {

	protected final static Logger LOGGER = Logger.getLogger(AbstractTest.class.getName());

	protected static EntityManagerFactory emf;
	protected static EntityManager em;

	private Long groupId;
	private Long employeeGroupId;
	private Long userId;
	private Long customerId;

	private List<Long> bookIds;

	private Long salesOrderId;

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

			Group employeeGroup = new Group();
			employeeGroup.setName("employee");
			em.persist(employeeGroup);

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
			creditCard.setType(CreditCardType.MASTER_CARD);
			creditCard.setExpirationMonth(12);
			creditCard.setExpirationYear(2017);
			creditCard.setNumber("5555 5555 5555 5555");
			customer.setCreditCard(creditCard);

			em.persist(customer);

			em.getTransaction().commit();

			fillBooks();

			groupId = group.getId();
			employeeGroupId = employeeGroup.getId();
			userId = user.getId();
			customerId = customer.getId();

			this.salesOrderId = this.getNewPersistedSalesOrder().getId();

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

			em.remove(em.find(SalesOrder.class, this.salesOrderId));

			User user = em.find(User.class, userId);
			em.remove(user);

			Group group = em.find(Group.class, groupId);
			em.remove(group);
			group = em.find(Group.class, employeeGroupId);
			em.remove(group);

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