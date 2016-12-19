package org.books.application.message;

import org.books.application.service.MailService;
import org.books.persistence.entity.SalesOrder;
import org.books.persistence.enumeration.OrderStatus;
import org.books.persistence.repository.OrderRepository;

import javax.annotation.Resource;
import javax.ejb.*;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.persistence.LockModeType;
import javax.transaction.UserTransaction;
import java.util.logging.Logger;

@MessageDriven(activationConfig = {
		@ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "jms/orderQueue"),
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue") })
@TransactionManagement(TransactionManagementType.BEAN)
public class OrderProcessor implements MessageListener {

	private Logger logger = Logger.getLogger(OrderProcessor.class.getSimpleName());

	@EJB
	private OrderRepository repository;

	@EJB
	private MailService mailService;

	@Resource
	private UserTransaction userTransaction;

	@Override
	public void onMessage(Message message) {
		try {
			if (message instanceof MapMessage) {
				MapMessage mapMessage = (MapMessage) message;
				long orderNr = mapMessage.getLong("orderNr");

				logger.info("onMessage, orderNr: " + orderNr);

				userTransaction.begin();
				SalesOrder order = repository.find(orderNr, LockModeType.PESSIMISTIC_WRITE);
				order.setStatus(OrderStatus.PROCESSING);
				userTransaction.commit();

				mailService.sendEmail(order.getCustomer().getEmail(), "Order " + order.getNumber(),
						"Order set to state processing");
			}
		} catch (Exception e) {
			logger.severe(e.toString());
		}
	}
}