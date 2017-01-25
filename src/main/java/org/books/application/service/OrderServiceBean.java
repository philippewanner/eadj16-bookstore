package org.books.application.service;

import org.books.application.dto.PurchaseOrder;
import org.books.application.dto.PurchaseOrderItem;
import org.books.application.enumeration.OrderProcessorType;
import org.books.application.exception.*;
import org.books.persistence.dto.BookInfo;
import org.books.persistence.dto.OrderInfo;
import org.books.persistence.entity.*;
import org.books.persistence.enumeration.CreditCardType;
import org.books.persistence.enumeration.OrderStatus;
import org.books.persistence.repository.BookRepository;
import org.books.persistence.repository.CustomerRepository;
import org.books.persistence.repository.OrderRepository;

import javax.annotation.Resource;
import javax.ejb.*;
import javax.inject.Inject;
import javax.jms.*;
import javax.persistence.LockModeType;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.books.application.exception.PaymentFailedException.Code;

/**
 * @author Philippe
 *
 * The service allows to: place an order, find an order by number search for
 * orders by customer and year cancel an order
 */
@Stateless(name = "OrderService")
@LocalBean
public class OrderServiceBean extends AbstractService implements OrderService {

	@EJB
	private OrderRepository orderRepository;

	@EJB
	private CustomerRepository customerRepository;

	@EJB
	private BookRepository bookRepository;

	@EJB
	private MailServiceBean mailService;

	@EJB
	private CatalogService catalogService;

	@Inject
	@JMSConnectionFactory("jms/connectionFactory")
	private JMSContext jmsContext;

	@Resource(lookup = "jms/orderQueue")
	private Queue queue;

	@Resource(name = "limitAmount")
	private Float limitAmount;

	@Override
	public void cancelOrder(Long orderNr) throws OrderNotFoundException, OrderAlreadyShippedException {

		SalesOrder orderFound = orderRepository.findByNumber(orderNr);
		if (orderFound == null) {

			logInfo("Order nr " + orderNr + " not found");
			throw new OrderNotFoundException();
		}
		if (orderFound.getStatus().equals(OrderStatus.SHIPPED)) {

			logInfo("Order nr " + orderNr + " already shipped");
			throw new OrderAlreadyShippedException();
		}

		orderFound.setStatus(OrderStatus.CANCELED);
		orderRepository.update(orderFound);
	}

	@Override
	public SalesOrder findOrder(Long orderNr) throws OrderNotFoundException {

		SalesOrder orderFound = orderRepository.findByNumber(orderNr);
		if (orderFound == null) {

			logInfo("Order nr " + orderNr + " not found");
			throw new OrderNotFoundException();
		}

		return orderFound;
	}

	@Override
	public SalesOrder placeOrder(PurchaseOrder purchaseOrder)
			throws CustomerNotFoundException, PaymentFailedException, BookNotFoundException {

		// Check if the customer exist
		Customer customer = customerRepository.find(purchaseOrder.getCustomerNr());
		if (customer == null) {
			throw new CustomerNotFoundException();
		}

		// Validates the credit card
		validateCreditCard(customer.getCreditCard());

		// Validates the limit amount allowed (defined in ejb-jar.xml)
		SalesOrder salesOrder = createSalesOrder(purchaseOrder);
		if (salesOrder.getAmount().floatValue() > limitAmount) {
			throw new PaymentFailedException(Code.PAYMENT_LIMIT_EXCEEDED);
		}

		orderRepository.persist(salesOrder);
		orderRepository.flush();

		sendToQueue(salesOrder.getNumber(), OrderProcessorType.STATE_TO_PROCESSING);

		return salesOrder;
	}

	@Override
	public List<OrderInfo> searchOrders(Long customerNr, Integer year) throws CustomerNotFoundException {

		Customer customer = customerRepository.find(customerNr);
		if (customer == null) {

			logInfo("Customer nr " + customerNr + " not found");
			throw new CustomerNotFoundException();
		}

		return orderRepository.searchByCustomerAndYear(customer, year);
	}

	@Schedule(minute = "*", hour = "*")
	public void shipOrders(Timer timer) {

		logInfo("********* Ship timer *********");

		List<SalesOrder> orders = orderRepository.findByStatus(OrderStatus.PROCESSING);
		for (SalesOrder order : orders) {
			try {
				orderRepository.lock(order, LockModeType.PESSIMISTIC_WRITE);
				order.setStatus(OrderStatus.SHIPPED);

				sendToQueue(order.getNumber(), OrderProcessorType.STATE_TO_SHIPPED);
			} catch (Exception e) {
				logger.severe(e.toString());
			}
		}
	}

	private void validateCreditCard(CreditCard creditCard) throws PaymentFailedException {

		final String VISA_REGEX = "^4[0-9]{12}(?:[0-9]{3})?$";
		final String MASTERCARD_REGEX =
				"^(?:5[1-5][0-9]{2}|222[1-9]|22[3-9][0-9]|2[3-6][0-9]{2}|27[01][0-9]|2720)" + "[0-9]{12}$";
		final String AMERICAN_EXPRESS_REGEX = "^3[47][0-9]{13}$";

		if (creditCard == null) {
			throw new PaymentFailedException(Code.INVALID_CREDIT_CARD);
		}

		CreditCardType creditCardType = creditCard.getType();
		switch (creditCardType) {
			case AMERICAN_EXPRESS:
				if (!creditCard.getNumber().matches(AMERICAN_EXPRESS_REGEX)) {
					throw new PaymentFailedException(Code.INVALID_CREDIT_CARD);
				}
				break;
			case MASTER_CARD:
				if (!creditCard.getNumber().matches(MASTERCARD_REGEX)) {
					throw new PaymentFailedException(Code.INVALID_CREDIT_CARD);
				}
				break;
			case VISA:
				if (!creditCard.getNumber().matches(VISA_REGEX)) {
					throw new PaymentFailedException(Code.INVALID_CREDIT_CARD);
				}
				break;
			default:
				throw new PaymentFailedException(Code.INVALID_CREDIT_CARD);
		}

		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String expirationString = String.format("%4d-%2d-%2d", creditCard.getExpirationYear(),
				creditCard.getExpirationMonth(), 1);

		try {
			Date expirationDate = sdf.parse(expirationString);
			if (expirationDate.before(now)) {
				throw new PaymentFailedException(Code.CREDIT_CARD_EXPIRED);
			}
		} catch (ParseException e) {
			logError("Cannot parse the credit card date");
		}

		// If comes here, the credit card is valid
	}

	private void sendToQueue(long orderNumber, OrderProcessorType type) {
		try {
			JMSProducer producer = jmsContext.createProducer();
			MapMessage msg = jmsContext.createMapMessage();
			msg.setString("type", type.toString());
			msg.setLong("orderNr", orderNumber);
			producer.send(queue, msg);
		} catch (JMSException e) {
			logError(e.toString());
		}
	}

	private SalesOrder createSalesOrder(PurchaseOrder po) throws BookNotFoundException {
		SalesOrder so = new SalesOrder();

		Customer c = customerRepository.find(po.getCustomerNr());
		so.setDate(new Date());

		so.setCustomer(c);
		so.setAddress(c.getAddress());
		so.setAmount(getAmount(po.getItems()));
		so.setCreditCard(c.getCreditCard());

		for (PurchaseOrderItem i : po.getItems()) {
			String isbnToSearch = i.getBookInfo().getIsbn();
			List<Book> foundBook = bookRepository.findByISBN(isbnToSearch);

			if ((foundBook == null) || (foundBook.size() == 0)) {

				Book bookToAdd = catalogService.findBook(isbnToSearch);
				bookRepository.persist(bookToAdd);

				//throw new BookNotFoundException();
			}
		}

		so.setSalesOrderItems(getSoItems(po.getItems()));
		so.setStatus(OrderStatus.ACCEPTED);

		return so;
	}

	private Set<SalesOrderItem> getSoItems(List<PurchaseOrderItem> items) {
		Set<SalesOrderItem> soItems = new HashSet<>();

		for (PurchaseOrderItem poi : items) {
			boolean added = false;
			for (SalesOrderItem soi : soItems) {

				if (soi.getBook().getIsbn().equals(poi.getBookInfo().getIsbn())) {
					Integer newQuantity = soi.getQuantity() + poi.getQuantity();
					BigDecimal newPrice
							= soi.getPrice().add(
							poi.getBookInfo().getPrice().multiply(BigDecimal.valueOf(poi.getQuantity())));

					soi.setPrice(newPrice);
					soi.setQuantity(newQuantity);
					added = true;
					break;
				}
			}
			if (!added) {
				SalesOrderItem newSoi = new SalesOrderItem();
				Book book = getBook(poi.getBookInfo());
				if (book != null) {
					newSoi.setBook(book);
					BigDecimal price = book.getPrice().multiply(BigDecimal.valueOf(poi.getQuantity()));
					newSoi.setPrice(price);
					newSoi.setQuantity(poi.getQuantity());

					soItems.add(newSoi);
				}
			}

		}

		return soItems;
	}

	private Book getBook(BookInfo bi) {
		List<Book> books = bookRepository.findByISBN(bi.getIsbn());

		if (books.size() > 0) {
			return books.get(0);
		}

		return null;
	}

	private BigDecimal getAmount(List<PurchaseOrderItem> items) {
		BigDecimal amount = BigDecimal.valueOf(0);

		for (PurchaseOrderItem i : items) {
			amount = amount.add(BigDecimal.valueOf(i.getQuantity()).multiply(i.getBookInfo().getPrice()));
		}
		return amount;
	}

	/*
	Unit Tests
	 */
	OrderRepository getOrderRepository() {
		return orderRepository;
	}
}