package org.books.application.message;

import org.books.application.enumeration.OrderProcessorType;
import org.books.application.service.MailService;
import org.books.persistence.entity.SalesOrder;
import org.books.persistence.enumeration.OrderStatus;
import org.books.persistence.repository.OrderRepository;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.persistence.LockModeType;
import java.util.logging.Logger;

@MessageDriven(activationConfig = {
		@ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "jms/orderQueue"),
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue") })
public class OrderProcessor implements MessageListener {

	private Logger logger = Logger.getLogger(OrderProcessor.class.getSimpleName());

	@EJB
	private OrderRepository repository;

	@EJB
	private MailService mailService;

	@Override
	public void onMessage(Message message) {
		try {
			if (message instanceof MapMessage) {
				MapMessage mapMessage = (MapMessage) message;

				OrderProcessorType type = (OrderProcessorType) mapMessage.getObject("type");
				long orderNr = mapMessage.getLong("orderNr");

				logger.info("onMessage, orderNr: " + orderNr + ", type: " + type);
				String text = null;

				SalesOrder order = repository.find(orderNr, LockModeType.PESSIMISTIC_WRITE);
				switch (type) {
					case STATE_TO_SHIPPED:
						text = "Order set to state shipped";
						break;
					case STATE_TO_PROCESSING:
						if (order.getStatus() == OrderStatus.ACCEPTED) {
							order.setStatus(OrderStatus.PROCESSING);
							text = "Order set to state processing";
						}
						break;
				}

				if (text != null) {
					mailService.sendEmail(order.getCustomer().getEmail(), "Order " + orderNr, text);
				}
			}
		} catch (Exception e) {
			logger.severe(e.toString());
		}
	}
}