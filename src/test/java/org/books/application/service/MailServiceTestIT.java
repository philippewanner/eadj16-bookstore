package org.books.application.service;

import org.books.application.exception.CustomerAlreadyExistsException;
import org.jboss.logging.Logger;
import org.testng.annotations.BeforeClass;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import static org.testng.Assert.assertNotNull;

public class MailServiceTestIT {

	private static final String ACCOUNT_SERVICE_NAME = "java:global/bookstore/MailService";

	private final static Logger LOGGER = Logger.getLogger(MailServiceTestIT.class.getName());

	private MailService service;

	@BeforeClass
	public void lookup() throws NamingException {
		service = (MailService) new InitialContext().lookup(ACCOUNT_SERVICE_NAME);
		assertNotNull(service);
	}

	// Ausgeklammert, damit nicht unnötig Mails gesendet werden
	//@Test
	public void simpleMail() throws CustomerAlreadyExistsException {
		LOGGER.info(">>>>>>>>>>>>>>>>>>> Mail simpleMail <<<<<<<<<<<<<<<<<<<<");
		service.sendEmail("lukas.kalt@gmail.com", "Hello", "It's me");
	}
}