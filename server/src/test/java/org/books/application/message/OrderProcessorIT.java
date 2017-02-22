package org.books.application.message;

import org.books.DbUtil;
import org.books.application.dto.PurchaseOrder;
import org.books.application.dto.PurchaseOrderItem;
import org.books.application.dto.Registration;
import org.books.application.enumeration.OrderProcessorType;
import org.books.application.exception.*;
import org.books.application.service.CatalogService;
import org.books.application.service.CustomerService;
import org.books.application.service.MailService;
import org.books.application.service.OrderService;
import org.books.persistence.TestDataProvider;
import org.books.persistence.dto.BookInfo;
import org.books.persistence.entity.*;
import org.books.persistence.enumeration.CreditCardType;
import org.books.persistence.enumeration.OrderStatus;
import org.books.persistence.repository.OrderRepository;
import org.jboss.logging.Logger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class OrderProcessorIT {

	private final static Logger LOGGER = Logger.getLogger(OrderProcessorIT.class.getName());

	// JNDI names
	private static final String ORDER_SERVICE_NAME = "java:global/bookstore/OrderService";
	private static final String CUSTOMER_SERVICE_NAME = "java:global/bookstore/CustomerService";
	private static final String CATALOG_SERVICE_NAME = "java:global/bookstore/CatalogService";

	private static final String MAIL_SERVICE_NAME = "java:global/bookstore/MailService";

	private static final String CONNECTION_FACTORY_NAME = "jms/connectionFactory";

	private static final String MASTERCARD_VALID_ACCOUNT_NUMBER = "5105105105105100";

	protected static EntityManagerFactory emf;
	protected static EntityManager em;

	private OrderService orderService;
	private CustomerService customerService;
	private CatalogService catalogService;

	private ConnectionFactory connectionFactory;
	private OrderProcessor orderProcessor;

	private Long orderNumber;

	@BeforeClass
	public void setup() throws SQLException, NamingException, PaymentFailedException, BookNotFoundException,
			CustomerNotFoundException {
		DbUtil.clearDatabase();

		emf = Persistence.createEntityManagerFactory("bookstore-db");
		em = emf.createEntityManager();


		OrderRepository orderRepository = new OrderRepository();
		orderRepository.setEntityManager(em);

		InitialContext initialContext = new InitialContext();
		orderService = (OrderService) initialContext.lookup(ORDER_SERVICE_NAME);
		assertNotNull(orderService);
		customerService = (CustomerService) initialContext.lookup(CUSTOMER_SERVICE_NAME);
		assertNotNull(customerService);
		catalogService = (CatalogService) initialContext.lookup(CATALOG_SERVICE_NAME);
		assertNotNull(catalogService);

		MailService mailService = (MailService) initialContext.lookup(MAIL_SERVICE_NAME);
		assertNotNull(mailService);

		connectionFactory = (ConnectionFactory) initialContext.lookup(CONNECTION_FACTORY_NAME);

		orderProcessor = new OrderProcessor();
		orderProcessor.setMailService(mailService);
		orderProcessor.setOrderRepository(orderRepository);

		addBooks();
		placeOrder();
	}

	@AfterClass
	public void tearDown() throws SQLException {
		DbUtil.clearDatabase();
	}

	@Test
	public void messageTest() throws JMSException, OrderNotFoundException {
		JMSContext context = connectionFactory.createContext();
		MapMessage message = context.createMapMessage();

		message.setString("type", OrderProcessorType.STATE_TO_PROCESSING.toString());
		message.setLong("orderNr", orderNumber);

		orderProcessor.onMessage(message);

		SalesOrder order = orderService.findOrder(orderNumber);
		assertNotNull(order);
		assertEquals(order.getStatus(), OrderStatus.PROCESSING);
	}

	private void addBooks() {
		List<Book> books = TestDataProvider.getBooks();
		for (Book b : books) {
			try {
				catalogService.addBook(b);
			} catch (BookAlreadyExistsException ex) {
				LOGGER.warn("book not added " + b.getTitle());
			}
		}
	}

	private Customer createNewCustomer() {
		return createNewCustomer(new CreditCard(CreditCardType.MASTER_CARD, MASTERCARD_VALID_ACCOUNT_NUMBER, 8, 2018));
	}

	private Customer createNewCustomer(CreditCard creditCard) {
		Address address = new Address("725 5th Avenue", "New York", "NY 10022", "NY", "United States");
		Customer customer = new Customer("Donald", "Trump", "Donald@Trump.org", address, creditCard);

		final String password = "md5";
		Registration registration = new Registration(customer, password);

		try {
			customer.setNumber(customerService.registerCustomer(registration));
			return customer;
		} catch (CustomerAlreadyExistsException e) {
			e.printStackTrace();
			return null;
		}
	}

	private List<PurchaseOrderItem> getPuchaseOrderItems() {
		List<PurchaseOrderItem> items = new ArrayList<>();
		List<BookInfo> books = catalogService.searchBooks("*");
		for (BookInfo b : books) {
			PurchaseOrderItem poi = new PurchaseOrderItem(b, 99);
			items.add(poi);
		}

		return items;
	}

	private void placeOrder() throws PaymentFailedException, BookNotFoundException, CustomerNotFoundException {
		// Given
		Customer newCustomer = this.createNewCustomer();
		PurchaseOrder purchaseOrder = new PurchaseOrder(newCustomer.getNumber(), getPuchaseOrderItems());

		// When
		SalesOrder salesOrderPersisted = orderService.placeOrder(purchaseOrder);
		orderNumber = salesOrderPersisted.getNumber();
	}
}