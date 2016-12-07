package org.books.persistence.repository;

import org.books.persistence.AbstractTest;
import org.books.persistence.dto.OrderInfo;
import org.books.persistence.entity.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class OrderRepositoryTest extends AbstractTest {

	private OrderRepository orderRepository;

	@BeforeClass
	public void setUpBeforeTest() {
		orderRepository = new OrderRepository();
		orderRepository.setEntityManager(em);
	}

	@Test
	public void searchByNumber_existingNumber() {

		// Given
		SalesOrder salesOrder = getPersistedSalesOrder();

		// When
		SalesOrder actualSalesOrder = orderRepository.findByNumber(2L);

		// Then
		assertNotNull(actualSalesOrder);
		assertEquals(salesOrder.getNumber(), actualSalesOrder.getNumber());
	}

	@Test
	public void searchByNumber_nonExistingNumber() {

		// Given
		// When
		SalesOrder actualSalesOrder = orderRepository.findByNumber(29L);

		// Then
		assertNull(actualSalesOrder);

	}

	@Test
	public void searchByCustomerAndYear_matchingCustomerAndYear() {

		// Given
		SalesOrder salesOrder = getPersistedSalesOrder();
		LocalDate expectedYear = salesOrder.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		Integer year = 1989;
		Customer customer = getPersistedCustomer();

		// When
		List<OrderInfo> foundOrderInfos = this.orderRepository.searchByCustomerAndYear(customer, year);

		// Then
		assertNotNull(foundOrderInfos);
		assertTrue(foundOrderInfos.size() > 0);
		LocalDate localDate = foundOrderInfos.get(0).getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		assertEquals(expectedYear.getYear(), localDate.getYear());
	}

	@Test
	public void sumUpByYear() {

		LOGGER.info(">>>>>>>>>>>>>>>>>>> sum up <<<<<<<<<<<<<<<<<<<<");

		Integer year = 1989;
		List<Object> foundOrderInfos = this.orderRepository.sumAmountNumberPositionsAvgAmountPerYear(year);

		StringBuilder out = new StringBuilder();

		out.append("\n-----------------------------------------------------------\n");
		out.append("Name      | sum amount |  # positions |  Avg/pos\n");

		for (Object ob : foundOrderInfos) {
			Object[] o = (Object[]) ob;
			out.append(String.format("%8s", (String) (o[0]))
					+ "  |   " + String.format("%6s", o[1])
					+ "  |   " + StringUtils.leftPad(Long.toString((Long) o[2]), 9)
					+ "  |  " + String.format("%8s", o[3]) + "\n");
		}

		out.append("-----------------------------------------------------------\n");

		LOGGER.info(out.toString());

		assertEquals(1, foundOrderInfos.size());
	}

	@Test
	public void searchByCustomerAndYear_onlyMatchingYear() {

		// Given
		Integer year = 1989;
		Customer customer = new Customer("fakeFirst", "fakeLast", "fakeEmail", new Address(), new CreditCard());

		// When
		List<OrderInfo> foundOrderInfos = this.orderRepository.searchByCustomerAndYear(customer, year);

		// Then
		assertNotNull(foundOrderInfos);
		assertEquals(0, foundOrderInfos.size());
	}

	@Test
	public void searchByCustomerAndYear_onlyMatchingCustomer() {
		// Given
		Integer year = 2011;
		Customer customer = new Customer("fakeFirst", "fakeLast", "fakeEmail", new Address(), new CreditCard());

		// When
		List<OrderInfo> foundOrderInfos = this.orderRepository.searchByCustomerAndYear(customer, year);

		// Then
		assertNotNull(foundOrderInfos);
		assertEquals(0, foundOrderInfos.size());
	}

	@Test
	public void searchByCustomerAndYear_notAtAllMatching() {
		// Given
		Integer year = 2011;
		Customer customer = getPersistedCustomer();

		// When
		List<OrderInfo> foundOrderInfos = this.orderRepository.searchByCustomerAndYear(customer, year);

		// Then
		assertNotNull(foundOrderInfos);
		assertEquals(0, foundOrderInfos.size());
	}
}
