package org.books.application.message;

import org.books.persistence.entity.SalesOrder;
import org.books.persistence.enumeration.OrderStatus;
import org.books.persistence.repository.OrderRepository;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import java.util.logging.Logger;

@MessageDriven(activationConfig = {
		@ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "jms/orderQueue"),
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue") })
public class OrderProcessor implements MessageListener {

	private Logger logger = Logger.getLogger(OrderProcessor.class.getSimpleName());

	@EJB
	private OrderRepository repository;

	@Override
	public void onMessage(Message message) {
		try {
			if (message instanceof MapMessage) {
				MapMessage mapMessage = (MapMessage) message;
				long orderId = mapMessage.getLong("orderId");

				SalesOrder order  = repository.find(orderId);
				if (order != null) {
					order.setStatus(OrderStatus.PROCESSING);
				}
			}
		} catch (JMSException e) {
			logger.severe(e.toString());
		}
	}
}