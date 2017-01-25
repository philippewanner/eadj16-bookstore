package org.books.application.service;

import javax.annotation.Resource;
import javax.ejb.Asynchronous;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.logging.Logger;

@Stateless(name = "MailService")
@LocalBean
public class MailServiceBean implements MailService {

	private static final Logger logger = Logger.getLogger(MailServiceBean.class.getSimpleName());

	@Resource(lookup = "mail/bookstore")
	private Session session;

	@Override
	@Asynchronous
	public void sendEmail(String emailTo, String subject, String text) {
		try {
			MimeMessage message = new MimeMessage(session);
			InternetAddress[] addresses = { new InternetAddress(emailTo)};
			message.setRecipients(Message.RecipientType.TO, addresses);
			message.setSubject(subject);
			message.setSentDate(new Date());
			message.setText(text);
			Transport.send(message);
		} catch (MessagingException e) {
			logger.severe(e.toString());
		}
	}
}